package io.live4.internal;

import static java.util.stream.Collectors.joining;

import java.io.File;
import java.io.PrintStream;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.lang.reflect.Parameter;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.lang.reflect.TypeVariable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Stream;

import org.junit.Ignore;
import org.junit.Test;
import org.stjs.javascript.Array;
import org.stjs.javascript.Map;
import org.stjs.javascript.SortFunction;
import org.stjs.javascript.functions.Callback1;
import org.stjs.javascript.functions.Function1;

import com.vg.js.bridge.Rx.Observable;

import io.live4.js.BaseAsyncDao;
import io.live4.js.CalendarApi;
import io.live4.model.Internal;
import io.live4.model.StreamResponse;
import io.live4.model.User;

public class TypeScriptTest {
    private static final String PADDING = "    ";
    
    @Test
    public void testGenerateAll() throws Exception {
        List<Class> excluded = Arrays.asList(Internal.class, BaseAsyncDao.class);
        
        List<Class> all = new ArrayList<>();
        all.addAll(ClassFinder.find("io.live4.model"));
        all.addAll(ClassFinder.find("io.live4.js"));
        all.removeAll(excluded);
        
        PrintStream out = new PrintStream(new File("index.d.ts"));
        out.println("import {Observable} from 'rx';");
        for (Class<?> class1 : all) {
            generate(class1, out);
        }
    }

    @Test
    @Ignore
    public void testName() throws Exception {
        Class cls = StreamResponse.class;
        generate(cls, System.err);
    }

    @Test
    @Ignore
    public void testSimple() throws Exception {
        Class cls = User.class;
        Method method = Stream.of(cls.getMethods()).filter(m -> m.getName().contains("getId")).findFirst().orElse(null);
        Type genericReturnType = method.getGenericReturnType();
        System.out.println(genericReturnType);
    }

    @Test
    @Ignore
    @SuppressWarnings({ "unused", "rawtypes" })
    public void testGenerics() throws Exception {
        Class cls = CalendarApi.class;
        Method method = Stream.of(cls.getMethods()).filter(m -> m.getName().contains("list")).findFirst().orElse(null);
        Type gt = method.getGenericReturnType();

        Class<?> declaringClass = method.getDeclaringClass();
        Type genericSuperclass = cls.getGenericSuperclass();
        Type[] actualTypes = actualTypeArgs(genericSuperclass).orElse(null);

        TypeVariable<?>[] genericTypes = declaringClass.getTypeParameters();

        Type mapped = mapType(gt, tv -> {
            for (int i = 0; i < genericTypes.length; i++) {
                if (tv.equals(genericTypes[i])) {
                    return actualTypes[i];
                }
            }
            System.out.println("cant resolve " + tv);
            return tv;
        });
        System.out.println(mapped);
    }

    private static Type mapType(Type t, Function<TypeVariable, Type> resolveGeneric) {
        if (t instanceof TypeVariable) {
            return resolveGeneric.apply((TypeVariable) t);
        }
        if (!(t instanceof ParameterizedType)) {
            return t;
        }
        ParameterizedType _gt = (ParameterizedType) t;
        Type[] actualTypeArguments = _gt.getActualTypeArguments();
        Type[] mapped = Stream.of(actualTypeArguments).map(_t -> mapType(_t, resolveGeneric)).toArray(x -> new Type[x]);
        PT pt = new PT(mapped, _gt.getRawType(), _gt.getOwnerType());
        return pt;
    }

    

    private void generate(Class cls, PrintStream out) {
        
        boolean synthetic = cls.isSynthetic();
        if (synthetic) {
            return;
        }
        if (cls.isAnnotation()) {
            //WARNING: ANNOTATIONS NOT SUPPORTED!!!
            return;
        }
        if (cls.isEnum()) {
            Object[] enumConstants = cls.getEnumConstants();
            String collect = Stream.of(enumConstants).map(x -> x.toString()).collect(joining(", "));
            out.printf("export enum %s { %s }\n", cls.getSimpleName(), collect);
            return;
        }
        {
            Class[] interfaces = cls.getInterfaces();
            String collect = Stream.of(interfaces).map(x -> x.getSimpleName()).collect(joining(", "));
            StringBuilder sb = new StringBuilder();
            sb.append("export ");
            if (!cls.isInterface()) {
                sb.append("class ");
            } else {
                sb.append("interface ");
            }
            sb.append(cls.getSimpleName());
            sb.append(" ");
            if (interfaces.length != 0 && cls.isInterface()) {
                sb.append("extends ");
                sb.append(collect);
            }
            if (interfaces.length != 0 && !cls.isInterface()) {
                sb.append("implements ");
                sb.append(collect);
            }
            sb.append(" {");
            out.println(sb.toString());
        }

        for (Constructor constructor : cls.getConstructors()) {
            int modifiers = constructor.getModifiers();
            if (!Modifier.isPublic(modifiers)) {
                continue;
            }
            Parameter[] parameters = constructor.getParameters();
            if (parameters.length > 0) {
                StringBuilder sb = new StringBuilder();
                sb.append(PADDING);
                sb.append("constructor(");
                //http://www.concretepage.com/java/jdk-8/java-8-reflection-access-to-parameter-names-of-method-and-constructor-with-maven-gradle-and-eclipse-using-parameters-compiler-argument#eclipse
                String collect = Stream.of(parameters).map(p -> {
                    return p.getName() + ": " + ts(p.getParameterizedType());
                }).collect(joining(", "));
                sb.append(collect);
                sb.append(")");
                sb.append(";");
                out.println(sb.toString());
                out.println();
            }
        }

        Field[] fields = cls.getFields();
//        Arrays.sort(fields, (f1, f2) -> {
//            int s1 = Modifier.isStatic(f1.getModifiers()) ? 1 : 0;
//            int s2 = Modifier.isStatic(f2.getModifiers()) ? 1 : 0;
//            return s2 - s1;
//        });

        for (Field field : fields) {
            int modifiers = field.getModifiers();
            if (!Modifier.isPublic(modifiers)) {
                continue;
            }
            if (field.isAnnotationPresent(Deprecated.class)) {
                continue;
            }

            StringBuilder sb = new StringBuilder();
            sb.append(PADDING);

            if (Modifier.isStatic(modifiers)) {
                sb.append("static ");
            }
            Type genericType = field.getGenericType();
            sb.append(field.getName());
            sb.append(": ");
            sb.append(ts(genericType));
            sb.append(";");
            out.println(sb.toString());
        }
        out.println();

        Method[] methods = cls.getMethods();
//        Arrays.sort(methods, (m1, m2) -> {
//            int s1 = Modifier.isStatic(m1.getModifiers()) ? 1 : 0;
//            int s2 = Modifier.isStatic(m2.getModifiers()) ? 1 : 0;
//            return s2 - s1;
//        });
        for (Method method : methods) {
            //            System.out.println(method);
            int modifiers = method.getModifiers();
            if (!Modifier.isPublic(modifiers)) {
                continue;
            }
            if (method.isAnnotationPresent(Deprecated.class)) {
                continue;
            }

            Class<?> declaringClass = method.getDeclaringClass();
            Type genericSuperclass = cls.getGenericSuperclass();
            Type[] actualTypes = actualTypeArgs(genericSuperclass).orElse(null);

            TypeVariable<?>[] genericTypes = declaringClass.getTypeParameters();
            Function<TypeVariable, Type> resolveGeneric = tv -> {
                for (int i = 0; i < genericTypes.length; i++) {
                    if (tv.equals(genericTypes[i])) {
                        return actualTypes[i];
                    }
                }
                System.out.println("cant resolve " + tv);
                return tv;
            };

            if (!declaringClass.equals(Object.class)) {
                //                System.out.println(method);
                //                System.out.println(declaringClass);
                StringBuilder sb = new StringBuilder();
                sb.append(PADDING);
                if (Modifier.isStatic(method.getModifiers())) {
                    sb.append("static ");
                }
                sb.append(method.getName());
                sb.append("(");
                //http://www.concretepage.com/java/jdk-8/java-8-reflection-access-to-parameter-names-of-method-and-constructor-with-maven-gradle-and-eclipse-using-parameters-compiler-argument#eclipse
                Parameter[] parameters = method.getParameters();
                String collect = Stream.of(parameters).map(p -> {
                    return p.getName() + ": " + ts(mapType(p.getParameterizedType(), resolveGeneric));
                }).collect(joining(", "));
                sb.append(collect);
                sb.append(")");
                Class<?> returnType = method.getReturnType();
                Type genericReturnType = method.getGenericReturnType();
                if (!void.class.equals(returnType)) {
                    sb.append(": ");
                    sb.append(ts(mapType(genericReturnType, resolveGeneric)));
                }
                sb.append(";");
                out.println(sb.toString());
            }
        }

        out.println("}");
    }

    static class PT implements ParameterizedType {
        Type[] args;
        Type raw;
        Type owner;

        public PT(Type[] args, Type raw, Type owner) {
            super();
            this.args = args;
            this.raw = raw;
            this.owner = owner;
        }

        @Override
        public Type[] getActualTypeArguments() {
            return args;
        }

        @Override
        public Type getRawType() {
            return raw;
        }

        @Override
        public Type getOwnerType() {
            return owner;
        }

        @Override
        public String toString() {
            StringBuilder sb = new StringBuilder();
            if (owner != null) {
                sb.append(owner);
                sb.append('.');
            }
            sb.append(raw);
            sb.append(Arrays.toString(args));
            return sb.toString();
        }
    }

    private static Optional<Type[]> actualTypeArgs(Type type) {
        return Optional.ofNullable(type).filter(gsc -> gsc instanceof ParameterizedType).map(c -> (ParameterizedType) c).map(
                pt -> pt.getActualTypeArguments());
    }

    private final static List<Class> number = Arrays.asList(int.class, long.class, double.class, float.class,
            byte.class, short.class, Integer.class, Long.class, Double.class, Short.class, Float.class);

    private String ts(Type _type) {
        if (_type instanceof Class) {
            Class type = (Class) _type;
            if (type.equals(String.class)) {
                return "string";
            }

            if (number.contains(type)) {
                return "number";
            }
            if (boolean.class.equals(type) || Boolean.class.equals(type)) {
                return "boolean";
            }

            if (type.isPrimitive()) {
                throw new IllegalStateException("TODO ts1 unhandled type " + type);
            }

            if (Map.class.equals(type)) {
                throw new IllegalStateException("TODO ts2 unhandled type " + type);
            } else if (Callback1.class.equals(type)) {
                return "(in: Object) => void";
            } else if (Array.class.equals(type)) {
                return "Object[]";
            }

            return type.getSimpleName();
        } else if (_type instanceof ParameterizedType) {
            ParameterizedType pgt = (ParameterizedType) _type;
            Type rawType = pgt.getRawType();
            if (Array.class.equals(rawType)) {
                Type[] actualTypeArguments = pgt.getActualTypeArguments();
                checkState(actualTypeArguments.length == 1, "Array expected 1 param");
                Type type2 = actualTypeArguments[0];
                return ts(type2) + "[]";
            } else if (Map.class.equals(rawType)) {
                Type[] actualTypeArguments = pgt.getActualTypeArguments();
                checkState(actualTypeArguments.length == 2, "Map expected 1 param");
                Type type2 = actualTypeArguments[1];
                return String.format("{[id: string]: %s}", ts(type2));
            } else if (SortFunction.class.equals(rawType)) {
                Type[] actualTypeArguments = pgt.getActualTypeArguments();
                checkState(actualTypeArguments.length == 1, "expected 1 param");
                String type = ts(actualTypeArguments[0]);
                return String.format("(h1: %s, h2: %s) => number", type, type);
            } else if (Function1.class.equals(rawType)) {
                Type[] actualTypeArguments = pgt.getActualTypeArguments();
                checkState(actualTypeArguments.length == 2, "expected 2 params");
                Type in = actualTypeArguments[0];
                Type out = actualTypeArguments[1];
                return String.format("(arg: %s) => %s", ts(in), ts(out));
            } else if (Callback1.class.equals(rawType)) {
                Type[] actualTypeArguments = pgt.getActualTypeArguments();
                checkState(actualTypeArguments.length == 1, "Callback1 expected 1 param");
                Type in = actualTypeArguments[0];
                return String.format("(arg: %s) => void", ts(in));
            } else if (Observable.class.equals(rawType)) {
                Type[] actualTypeArguments = pgt.getActualTypeArguments();
                checkState(actualTypeArguments.length == 1, "Observable expected 1 param");
                return String.format("Observable<%s>", ts(actualTypeArguments[0]));
            } else {
                System.out.println("unhandled " + pgt);
                return ts(pgt.getRawType());
            }
        } else if (_type instanceof TypeVariable) {
            return "Object";
        } else {
            throw new IllegalStateException("TODO5");
        }
    }

    public static void checkState(boolean expression, Object errorMessage) {
        if (!expression) {
            throw new IllegalStateException(String.valueOf(errorMessage));
        }
    }

}
