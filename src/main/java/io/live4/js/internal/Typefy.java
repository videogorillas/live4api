package io.live4.js.internal;

import static org.stjs.javascript.Array.isArray;
import static org.stjs.javascript.Global.console;
import static org.stjs.javascript.JSGlobal.Boolean;
import static org.stjs.javascript.JSGlobal.eval;
import static org.stjs.javascript.JSGlobal.typeof;
import static org.stjs.javascript.JSObjectAdapter.$get;
import static org.stjs.javascript.JSObjectAdapter.$properties;
import static org.stjs.javascript.JSObjectAdapter.$prototype;
import static org.stjs.javascript.JSObjectAdapter.$put;
import static org.stjs.javascript.JSObjectAdapter.hasOwnProperty;

import org.stjs.javascript.Array;
import org.stjs.javascript.Date;
import org.stjs.javascript.JSCollections;
import org.stjs.javascript.JSObjectAdapter;
import org.stjs.javascript.Map;
import org.stjs.javascript.annotation.Namespace;

/**
 * Usage:
 *
 * VGJS.typefy
 *
 * <pre>
 *  Object obj = JSON.parse("{\"email\": \"zhuker@videogorillas.com\"}");
 *  User user = VGJS.typefy(obj, User.class);
 *  console.log(user.getEmail())
 * </pre>
 *
 * VGJS.typefyArray
 *
 * <pre>
 *
 *  Object obj = JSON.parse("[{\"email\": \"zhuker@videogorillas.com\"}]");
 *  Array<User> users = VGJS.typefyArray(obj, User.class);
 *  console.log(users.$get(0).getEmail())
 *
 * </pre>
 *
 * @author zhukov
 *
 */
@Namespace("live4api")
public class Typefy {

    private static Class elementType(Class type) {
        if (typeof(type) == "function")
            return type;

        if (Boolean($get(type, "arguments"))) {
            return eval((String) ((Array) $get(type, "arguments")).$get(0));
        }

        if (typeof(type) == "string")
            return eval((String) (Object) type);
        return Object.class;
    }

    private static <T> T getEnum(String enumClassName, String enumEntryName) {
        Object enumClass = eval(enumClassName);
        return (T) $get(enumClass, enumEntryName);
    }

    private static Object convert(Object td, Object json) {
        if (!Boolean(td)) {
            return json;
        }
        if (hasOwnProperty(td, "name")) {
            String name = (String) $get(td, "name");
            Array<String> args = (Array<String>) $get(td, "arguments");
            if ("Enum".equals(name) && args != null && args.$length() > 0) {
                return getEnum(args.$get(0), (String) json);
            }
        }
        if (Date.class == td || "Date" == td) {
            return new Date((String) json);
        }
        console.log("td", typeof(td), td, json);
        throw new RuntimeException("dont know what to do");
    }

    public static <T> Array<T> typefyArray(Object obj, Class<T> cls) {
        if (isArray(obj)) {
            Array<T> result = JSCollections.$array();
            Array arr = (Array) obj;
            for (int idx = 0; idx < arr.$length(); idx++) {
                result.push(typefy(arr.$get(idx), cls));
            }
            return result;
        }
        throw new RuntimeException("array expected got " + typeof(obj) + " instead");
    }

    public static <T> T typefy(Object obj, Class<T> cls) {
        if (isArray(obj)) {
            Array result = JSCollections.$array();
            Array _obj = (Array) obj;
            for (int idx = 0; idx < _obj.$length(); idx++) {
                result.push(typefy(_obj.$get(idx), elementType(cls)));
            }
            return (T) result;
        }

        if (obj == null)
            return null;

        Object ret = null;
        if (cls == null) {
            // WAT
            return (T) obj;
        } else if (typeof(cls) == "string") {
            ret = eval("new " + cls + "();");
            cls = (Class) JSObjectAdapter.$constructor(ret);
        } else if (typeof(cls) == "object" && "Map" == $get(cls, "name")) {
            //MAP
            Array<String> args = (Array<String>) $get(cls, "arguments");
            cls = eval(args.$get(1));
            ret = obj;
            Map<String, Object> map = (Map<String, Object>) obj;
            for (String key : map) {
                Object prop = map.$get(key);
                if (prop == null)
                    continue;
                if (typeof(prop) == "string") {
                    $put(ret, key, convert(cls, prop));
                } else if (typeof(prop) == "object") {
                    $put(ret, key, typefy(prop, cls));
                }
            }
            return (T) map;
        } else if (typeof(cls) == "object" && "Enum" == $get(cls, "name")) {
            Array<String> args = (Array<String>) $get(cls, "arguments");
            return getEnum(args.$get(0), (String) $get(obj, "_name"));
        } else {
            ret = InternalJSAdapter.create(Object.class, $prototype(cls));
        }

        for (String key : $properties(ret)) {
            Object prop = JSObjectAdapter.$get(obj, key);
            if (prop == null)
                continue;
            Map<String, Object> _td = (Map<String, Object>) $get(cls, "$typeDescription");
            Object td = _td != null ? _td.$get(key) : null;
            if (!Boolean(td)) {
                $put(ret, key, prop);
            }
            if (typeof(prop) == "string") {
                $put(ret, key, convert(td, prop));
            } else if (typeof(prop) == "object") {
                $put(ret, key, typefy(prop, (Class) td));
            }

        }
        return (T) ret;
    }
}
