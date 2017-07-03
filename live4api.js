/*
 *  Copyright 2011 Alexandru Craciun, Eyal Kaspi
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */
/**** Functionality in Java, but not in JS ********
 * methods added to JS prototypes
 */

var stjs={};

stjs.NOT_IMPLEMENTED = function(){
	throw "This method is not implemented in Javascript.";
};

stjs.JavalikeEquals = function(value){
	if (value == null)
		return false;
	if (value.valueOf)
		return this.valueOf() === value.valueOf();
	return this === value;
};

stjs.JavalikeGetClass = function(){
	return this.constructor;
};

/* String */
if (!String.prototype.equals) {
	String.prototype.equals=stjs.JavalikeEquals;
}
if (!String.prototype.getBytes) {
	String.prototype.getBytes=stjs.NOT_IMPLEMENTED;
}
if (!String.prototype.getChars) {
	String.prototype.getChars=stjs.NOT_IMPLEMENTED;
}
if (!String.prototype.contentEquals){
	String.prototype.contentEquals=stjs.NOT_IMPLEMENTED;
}
if (!String.prototype.startsWith) {
	String.prototype.startsWith=function(start, from){
		var f = from != null ? from : 0;
		return this.substring(f, f + start.length) == start;
	}
}
if (!String.prototype.endsWith) {
	String.prototype.endsWith=function(end){
		if (end == null)
			return false;
		if (this.length < end.length)
			return false;
		return this.substring(this.length - end.length, this.length) == end;
	}
}
if (!String.prototype.trim) {
	stjs.trimLeftRegExp = /^\s+/;
	stjs.trimRightRegExp = /\s+$/;
	String.prototype.trim = function() {
		return this.replace(stjs.trimLeftRegExp, "").replace(stjs.trimRightRegExp, "");
	}
}
if (!String.prototype.matches){
	String.prototype.matches=function(regexp){
		return this.match("^" + regexp + "$") != null;
	}
}
if (!String.prototype.compareTo){
	String.prototype.compareTo=function(other){
		if (other == null)
			return 1;
		if (this < other)
			return -1;
		if (this == other)
			return 0;
		return 1;
	}
}

if (!String.prototype.compareToIgnoreCase){
	String.prototype.compareToIgnoreCase=function(other){
		if (other == null)
			return 1;
		return this.toLowerCase().compareTo(other.toLowerCase());
	}
}

if (!String.prototype.equalsIgnoreCase){
	String.prototype.equalsIgnoreCase=function(other){
		if (other == null)
			return false;
		return this.toLowerCase() === other.toLowerCase();
	}
}

if (!String.prototype.codePointAt){
	String.prototype.codePointAt=String.prototype.charCodeAt;
}

if (!String.prototype.codePointBefore){
	String.prototype.codePointBefore=stjs.NOT_IMPLEMENTED;
}
if (!String.prototype.codePointCount){
	String.prototype.codePointCount=stjs.NOT_IMPLEMENTED;
}

if (!String.prototype.replaceAll){
	String.prototype.replaceAll=function(regexp, replace){
		return this.replace(new RegExp(regexp, "g"), replace);
	}
}

if (!String.prototype.replaceFirst){
	String.prototype.replaceFirst=function(regexp, replace){
		return this.replace(new RegExp(regexp), replace);
	}
}

if (!String.prototype.regionMatches){
	String.prototype.regionMatches=function(ignoreCase, toffset, other, ooffset, len){
		if (arguments.length == 4){
			len=arguments[3];
			ooffset=arguments[2];
			other=arguments[1];
			toffset=arguments[0];
			ignoreCase=false;
		}
		if (toffset < 0 || ooffset < 0 || other == null || toffset + len > this.length || ooffset + len > other.length)
			return false;
		var s1 = this.substring(toffset, toffset + len);
		var s2 = other.substring(ooffset, ooffset + len);
		return ignoreCase ? s1.equalsIgnoreCase(s2) : s1 === s2;
	}
}

if(!String.prototype.contains){
	String.prototype.contains=function(it){
		return this.indexOf(it)>=0;
	};
}

if(!String.prototype.getClass){
	String.prototype.getClass=stjs.JavalikeGetClass;
}


//force valueof to match the Java's behavior
String.valueOf=function(value){
	return new String(value);
};

/* Number */
var Byte=Number;
var Double=Number;
var Float=Number;
var Integer=Number;
var Long=Number;
var Short=Number;

/* type conversion - approximative as Javascript only has integers and doubles */
if (!Number.prototype.intValue) {
	Number.prototype.intValue=function(){
		return parseInt(this);
	}
}
if (!Number.prototype.shortValue) {
	Number.prototype.shortValue=function(){
		return parseInt(this);
	}
}
if (!Number.prototype.longValue) {
	Number.prototype.longValue=function(){
		return parseInt(this);
	}
}
if (!Number.prototype.byteValue) {
	Number.prototype.byteValue=function(){
		return parseInt(this);
	}
}

if (!Number.prototype.floatValue) {
	Number.prototype.floatValue=function(){
		return parseFloat(this);
	}
}

if (!Number.prototype.doubleValue) {
	Number.prototype.doubleValue=function(){
		return parseFloat(this);
	}
}

if (!Number.parseInt) {
	Number.parseInt = parseInt;
}
if (!Number.parseShort) {
	Number.parseShort = parseInt;
}
if (!Number.parseLong) {
	Number.parseLong = parseInt;
}
if (!Number.parseByte) {
	Number.parseByte = parseInt;
}

if (!Number.parseDouble) {
	Number.parseDouble = parseFloat;
}

if (!Number.parseFloat) {
	Number.parseFloat = parseFloat;
}

if (!Number.isNaN) {
	Number.isNaN = isNaN;
}

if (!Number.prototype.isNaN) {
	Number.prototype.isNaN = function() {
		return isNaN(this);
	}
}
if (!Number.prototype.equals) {
	Number.prototype.equals=stjs.JavalikeEquals;
}
if(!Number.prototype.getClass){
	Number.prototype.getClass=stjs.JavalikeGetClass;
}

//force valueof to match approximately the Java's behavior (for Integer.valueOf it returns in fact a double)
Number.valueOf=function(value){
	return new Number(value).valueOf();
}

/* Boolean */
if (!Boolean.prototype.equals) {
	Boolean.prototype.equals=stjs.JavalikeEquals;
}
if(!Boolean.prototype.getClass){
	Boolean.prototype.getClass=stjs.JavalikeGetClass;
}

//force valueof to match the Java's behavior
Boolean.valueOf=function(value){
	return new Boolean(value).valueOf();
}



/************* STJS helper functions ***************/
stjs.global=this;
stjs.skipCopy = {"prototype":true, "constructor": true, "$typeDescription":true, "$inherit" : true};

stjs.ns=function(path){
	var p = path.split(".");
	var obj = stjs.global;
	for(var i = 0; i < p.length; ++i){
		var part = p[i];
		obj = obj[part] = obj[part] || {};
	}
	return obj;
};

stjs.copyProps=function(from, to){
	for(var key in from){
		if (!stjs.skipCopy[key])
			to[key]	= from[key];
	}
	return to;
};

stjs.copyInexistentProps=function(from, to){
	for(var key in from){
		if (!stjs.skipCopy[key] && !to[key])
			to[key]	= from[key];
	}
	return to;
};

stjs.extend=function(_constructor, _super, _implements, _initializer, _typeDescription, _annotations){
	if(typeof(_typeDescription) !== "object"){
		// stjs 1.3+ always passes an non-null object to _typeDescription => The code calling stjs.extend
		// was generated with version 1.2 or earlier, so let's call the 1.2 version of stjs.extend
		return stjs.extend12.apply(this, arguments);
	}

	_constructor.$inherit=[];

	if(_super != null){
		// I is used as a no-op constructor that has the same prototype as _super
		// we do this because we cannot predict the result of calling new _super()
		// without parameters (it might throw an exception).
		// Basically, the following 3 lines are a safe equivalent for
		// _constructor.prototype = new _super();
		var I = function(){};
		I.prototype	= _super.prototype;
		_constructor.prototype	= new I();

		// copy static properties for super
		// assign every method from proto instance
		stjs.copyProps(_super, _constructor);
		stjs.copyProps(_super.$typeDescription, _typeDescription);
		stjs.copyProps(_super.$annotations, _annotations);

		//add the super class to inherit array
		_constructor.$inherit.push(_super);
	}

	// copy static properties and default methods from interfaces
	for(var a = 0; a < _implements.length; ++a){
		if (!_implements[a]) continue;
		stjs.copyProps(_implements[a], _constructor);
		stjs.copyInexistentProps(_implements[a].prototype, _constructor.prototype);
		_constructor.$inherit.push(_implements[a]);
	}

	// remember the correct constructor
	_constructor.prototype.constructor	= _constructor;

	// run the initializer to assign all static and instance variables/functions
	if(_initializer != null){
		_initializer(_constructor, _constructor.prototype);
	}

	_constructor.$typeDescription = _typeDescription;
	_constructor.$annotations = _annotations;

	// add the default equals method if it is not present yet, and we don't have a superclass
	if(_super == null){
		if(!_constructor.prototype.equals) {
			_constructor.prototype.equals = stjs.JavalikeEquals;
		}
		if(!_constructor.prototype.getClass) {
			_constructor.prototype.getClass = stjs.JavalikeGetClass;
		}
	}

	// build package and assign
	return	_constructor;
};

/**
 * 1.2 and earlier version of stjs.extend. Included for backwards compatibility
 */
stjs.extend12=function( _constructor,  _super, _implements){
	var I = function(){};
	I.prototype	= _super.prototype;
	_constructor.prototype	= new I();

	//copy static properties for super and interfaces
	// assign every method from proto instance
	for(var a = 1; a < arguments.length; ++a){
		stjs.copyProps(arguments[a], _constructor);
	}
	// remember the correct constructor
	_constructor.prototype.constructor	= _constructor;

	// add the default equals method if we don't have a superclass. Code generated with version 1.2 will
	// override this method is equals() is present in the original java code.
	// this was not part of the original 1.2 version of extends, however forward compatibility
	// with 1.3 requires it
	if(_super == null){
		_constructor.prototype.equals = stjs.JavalikeEquals;
		_constructor.prototype.getClass = stjs.JavalikeGetClass;
	}

	// build package and assign
	return	_constructor;
};

/**
 * return type's annotations
 */
stjs.getAnnotations = function(clz) {
	return clz.$annotations;
};

stjs.getTypeAnnotation = function(clz, annType) {
	var ann = clz.$annotations._;
	return ann ? ann[annType]: null;
};

stjs.getMemberAnnotation = function(clz, memberName, annType) {
	var ann = clz.$annotations.memberName;
	return ann ? ann[annType]: null;
};

stjs.getParameterAnnotation = function(clz, methodName, idx, annType) {
	var ann = clz.$annotations[methodName + "$" + idx];
	return ann ? ann[annType]: null;
};

/**
 * checks if the child is an instanceof parent. it checks recursively if "parent" is the child itself or it's found somewhere in the $inherit array
 */
stjs.isInstanceOf=function(child, parent){
	if (child == null)
		return false;
	if (child === parent)
		return true;
	if (!child.$inherit)
		return false;
	for(var i = 0; i < child.$inherit.length; ++i){
		if (stjs.isInstanceOf(child.$inherit[i], parent)) {
			return true;
		}
	}
	return false;
}

stjs.enumEntry=function(idx, name){
	this._name = name;
	this._ordinal = idx;
};

stjs.enumEntry.prototype.name=function(){
	return this._name;
};
stjs.enumEntry.prototype.ordinal=function(){
	return this._ordinal;
};
stjs.enumEntry.prototype.toString=function(){
	return this._name;
};
stjs.enumEntry.prototype.equals=stjs.JavalikeEquals;

stjs.enumeration=function(){
	var i;
	var e = {};
	e._values = [];
	for(i = 0; i < arguments.length; ++i){
		e[arguments[i]] = new stjs.enumEntry(i, arguments[i]);
		e._values[i] = e[arguments[i]];
	}
	e.values = function(){return this._values;};
	e.valueOf=function(label){
		return this[label];
	}
	return e;
};


/**
 * if true the execution of generated main methods is disabled.
 * this is useful when executing unit tests, to no have the main methods executing before the tests
 */
stjs.mainCallDisabled = false;

stjs.exception=function(err){
	return err;
};

stjs.isEnum=function(obj){
	return obj != null && obj.constructor == stjs.enumEntry;
};

if (typeof Math.trunc === "function") {
	stjs.trunc = Math.trunc;
} else {
	stjs.trunc=function(n) {
		if (n == null)
			return null;
		return n >= 0 ? Math.floor(n) : Math.ceil(n);
	}
}

stjs.converters = {
	Date : function(s, type) {
		var a = /^(\d{4})-(\d{2})-(\d{2}) (\d{2}):(\d{2}):(\d{2}(?:\.\d*)?)$/
				.exec(s);
		if (a) {
			return new Date(+a[1], +a[2] - 1, +a[3], +a[4], +a[5], +a[6]);
		}
		return null;
	},

	Enum : function(s, type){
		return eval(type.arguments[0])[s];
	}
};


stjs.serializers = {
	Date : function(d, type) {
		function pad(n){
			return n < 10 ? "0" + n : "" + n;
		}
		if (d) {
			return "" + d.getFullYear() + "-" + pad(d.getMonth()+1) + "-" + pad(d.getDate()) + " " + pad(d.getHours()) + ":" + pad(d.getMinutes()) + ":" + pad(d.getSeconds());
		}
		return null;
	},

	Enum : function(e, type){
		return e != null ? e.toString() : null;
	}
};

/**
 * Used to be able to send method references and lambdas that capture 'this' as callbacks.
 * This method has a bunch of different usage patterns:
 *
 * bind(instance, "methodName"):
 *     Used when translating a capturing method reference (eg: instance::methodName), or when translating
 *
 * bind(this, function):
 *     Used when translating a lambda that uses 'this' explicitly or implicitly (eg: () -> this.doSomething(3))
 *
 * bind(this, function, specialTHISparamPosition)
 *     Used when translating a lambda that uses the special all-caps 'THIS' parameter
 *
 * bind("methodName")
 *     Used when translating a non-static method reference (eg: TypeName::methodName, where methodName is non-static)
 */
stjs.bind=function(obj, method, thisParamPos) {
	var useFirstParamAsContext = false;
	if(method == null) {
		// the bind("methodName") is the only usage where the method argument can be null
		method = obj;
		obj = null;
		useFirstParamAsContext = true;
	}
	var addThisToParameters = thisParamPos != null;

	var f = function(){
		var args = arguments;
		if (addThisToParameters) {
			Array.prototype.splice.call(args, thisParamPos, 0, this);
		}
		if(useFirstParamAsContext){
			obj = Array.prototype.shift.call(args);
		}

		if (typeof method === "string") {
			return obj[method].apply(obj, args);

		} else {
			return method.apply(obj, args);
		}
	};
	return f;
};


/** *********** global ************** */
function exception(err){
	return err;
}

function isEnum(obj){
	return obj != null && obj.constructor == stjs.enumEntry;
}

/******* parsing *************/

/**
 * parse a json string using the type definition to build a typed object hierarchy
 */
stjs.parseJSON = (function () {
	  var number
	      = '(?:-?\\b(?:0|[1-9][0-9]*)(?:\\.[0-9]+)?(?:[eE][+-]?[0-9]+)?\\b)';
	  var oneChar = '(?:[^\\0-\\x08\\x0a-\\x1f\"\\\\]'
	      + '|\\\\(?:[\"/\\\\bfnrt]|u[0-9A-Fa-f]{4}))';
	  var string = '(?:\"' + oneChar + '*\")';

	  // Will match a value in a well-formed JSON file.
	  // If the input is not well-formed, may match strangely, but not in an unsafe
	  // way.
	  // Since this only matches value tokens, it does not match whitespace, colons,
	  // or commas.
	  var jsonToken = new RegExp(
	      '(?:false|true|null|[\\{\\}\\[\\]]'
	      + '|' + number
	      + '|' + string
	      + ')', 'g');

	  // Matches escape sequences in a string literal
	  var escapeSequence = new RegExp('\\\\(?:([^u])|u(.{4}))', 'g');

	  // Decodes escape sequences in object literals
	  var escapes = {
	    '"': '"',
	    '/': '/',
	    '\\': '\\',
	    'b': '\b',
	    'f': '\f',
	    'n': '\n',
	    'r': '\r',
	    't': '\t'
	  };
	  function unescapeOne(_, ch, hex) {
	    return ch ? escapes[ch] : String.fromCharCode(parseInt(hex, 16));
	  }

	  var constructors = {};

	  function constr(name, param){
		  var c = constructors[name];
		  if (!c)
			  constructors[name] = c = eval(name);
		  return new c(param);
	  }

	  function convert(type, json){
		  if (!type)
			  return json;
		  var cv = stjs.converters[type.name || type];
		  if (cv)
			  return cv(json, type);
		  //hopefully the type has a string constructor
		 return constr(type, json);
	  }

	  function builder(type){
		  if (!type)
			  return {};
			if (typeof type == "function")
				return new type();
			if (type.name) {
				if (type.name == "Map")
					return {};
				if (type.name == "Array")
					return [];
				return constr(type.name);
			}
			return constr(type);
	  }

	  // A non-falsy value that coerces to the empty string when used as a key.
	  var EMPTY_STRING = new String('');
	  var SLASH = '\\';

	  // Constructor to use based on an open token.
	  var firstTokenCtors = { '{': Object, '[': Array };

	  var hop = Object.hasOwnProperty;

	  function nextMatch(str){
		  var m = jsonToken.exec(str);
		  return m != null ? m[0] : null;
	  }
	  return function (json, type) {
	    // Split into tokens
	    // Construct the object to return
	    var result;
	    var tok = nextMatch(json);
	    var topLevelPrimitive = false;
	    if ('{' === tok) {
	      result = builder(type, null);
	    } else if ('[' === tok) {
	      result = [];
	    } else {
	      // The RFC only allows arrays or objects at the top level, but the JSON.parse
	      // defined by the EcmaScript 5 draft does allow strings, booleans, numbers, and null
	      // at the top level.
	      result = [];
	      topLevelPrimitive = true;
	    }

	    // If undefined, the key in an object key/value record to use for the next
	    // value parsed.
	    var key;
	    // Loop over remaining tokens maintaining a stack of uncompleted objects and
	    // arrays.
	    var stack = [result];
	    var stack2 = [type];
	    for (tok = nextMatch(json); tok != null; tok = nextMatch(json)) {

	      var cont;
	      switch (tok.charCodeAt(0)) {
	        default:  // sign or digit
	          cont = stack[0];
	          cont[key || cont.length] = +(tok);
	          key = void 0;
	          break;
	        case 0x22:  // '"'
	          tok = tok.substring(1, tok.length - 1);
	          if (tok.indexOf(SLASH) !== -1) {
	            tok = tok.replace(escapeSequence, unescapeOne);
	          }
	          cont = stack[0];
	          if (!key) {
	            if (cont instanceof Array) {
	              key = cont.length;
	            } else {
	              key = tok || EMPTY_STRING;  // Use as key for next value seen.
	              stack2[0] = cont.constructor.$typeDescription ? cont.constructor.$typeDescription[key] : stack2[1].arguments[1];
	              break;
	            }
	          }
	          cont[key] = convert(stack2[0],tok);
	          key = void 0;
	          break;
	        case 0x5b:  // '['
	          cont = stack[0];
	          stack.unshift(cont[key || cont.length] = []);
	          stack2.unshift(stack2[0].arguments[0]);
	          //put the element type desc
	          key = void 0;
	          break;
	        case 0x5d:  // ']'
	          stack.shift();
	          stack2.shift();
	          break;
	        case 0x66:  // 'f'
	          cont = stack[0];
	          cont[key || cont.length] = false;
	          key = void 0;
	          break;
	        case 0x6e:  // 'n'
	          cont = stack[0];
	          cont[key || cont.length] = null;
	          key = void 0;
	          break;
	        case 0x74:  // 't'
	          cont = stack[0];
	          cont[key || cont.length] = true;
	          key = void 0;
	          break;
	        case 0x7b:  // '{'
	          cont = stack[0];
	          stack.unshift(cont[key || cont.length] = builder(stack2[0]));
	          stack2.unshift(null);
	          key = void 0;
	          break;
	        case 0x7d:  // '}'
	          stack.shift();
	          stack2.shift();
	          break;
	      }
	    }
	    // Fail if we've got an uncompleted object.
	    if (topLevelPrimitive) {
	      if (stack.length !== 1) { throw new Error(); }
	      result = result[0];
	    } else {
	      if (stack.length) { throw new Error(); }
	    }

	    return result;
	  };
})();




stjs.isArray=function( obj ) {
    return stjs.toString.call(obj) === "[object Array]";
};

/**
 * cls can by the type of the return.
 * If it's an array it can be either the type of an element or the type definition of the field.
 * TODO - for other collections and classes is not done yet
 */
stjs.typefy=function(obj, cls){
	if (stjs.isArray(obj)){
		var result = [];
		for(var idx = 0; idx < obj.length; idx++){
			result.push(stjs.typefy(obj[idx], elementType(cls)));
		}
		return result;
	}
	 var constructors = {};
	 function constr(name, param){
		  var c = constructors[name];
		  if (!c)
			  constructors[name] = c = eval(name);
		  return new c(param);
	  }

	 function elementType(type){
		 if (typeof type == "function")
			 return type;
		 if (type.arguments) {
			 return eval(type.arguments[0]);
		 }
		 if (typeof type == "string")
			 return eval(type);
		 return Object;
	  }


	function convert(type, json){
		  if (!type)
			  return json;
		  var cv = stjs.converters[type.name || type];
		  if (cv)
			  return cv(json, type);
		  //hopefully the type has a string constructor
		 return constr(type, json);
	  }

	 function builder(type){
		  if (!type)
			  return {};
			if (typeof type == "function")
				return new type();
			if (type.name) {
				if (type.name == "Map")
					return {};
				if (type.name == "Array")
					return [];
				return constr(type.name);
			}
			return constr(type);
	  }

	  if (obj == null)
		  return null;

	  var ret = new cls();
	  for(var key in obj){
		  var prop = obj[key];
		  if (prop == null)
			  continue;
		  var td = cls.$typeDescription[key];
		  if (!td) {
			  ret[key] = prop;
			  continue;
		  }
		  if (typeof prop == "string")
			ret[key] = convert(td, prop);
		  else if (typeof prop == "object") {
				if (typeof td == "string") {
					td = eval(td);
		  		}
				ret[key] = stjs.typefy(prop, td);
			}
	  }
	  return ret;
};
stjs.hydrate=stjs.typefy

stjs.stringify=function(obj, cls){
	 if (obj == null)
		  return null;

	 var ret = {};
	  for(var key in obj){
		  var td = cls.$typeDescription[key];
		  var prop = obj[key];
		  var ser = td != null ? stjs.serializers[td.name || td] : null;

		  if (typeof prop == "function")
			  continue;

		  if (!td || !ser) {
			  ret[key] = prop;
			  continue;
		  }
		  if (typeof prop != "string")
			  if (ser)
				  ret[key] = ser(prop, td);
			  else
				  ret[key] = stjs.typefy(prop, td);
	  }
	  return ret;
};
/************* STJS asserts ***************/
stjs.assertHandler = function(position, code, msg) {
	throw msg + " at " + position;
};

stjs.STJSAssert = {};

stjs.STJSAssert.setAssertHandler = function(a) {
	stjs.assertHandler = a;
}

stjs.STJSAssert.assertArgEquals = function(position, code, expectedValue, testValue) {
	if (expectedValue != testValue && stjs.assertHandler)
		stjs.assertHandler(position, code, "Wrong argument. Expected: " + expectedValue + ", got:" + testValue);
}

stjs.STJSAssert.assertArgNotNull = function(position, code, testValue) {
	if (testValue == null && stjs.assertHandler)
		stjs.assertHandler(position, code, "Wrong argument. Null value");
}

stjs.STJSAssert.assertArgTrue = function(position, code, condition) {
	if (!condition && stjs.assertHandler)
		stjs.assertHandler(position, code, "Wrong argument. Condition is false");
}

stjs.STJSAssert.assertStateEquals = function(position, code, expectedValue, testValue) {
	if (expectedValue != testValue && stjs.assertHandler)
		stjs.assertHandler(position, code, "Wrong state. Expected: " + expectedValue + ", got:" + testValue);
}

stjs.STJSAssert.assertStateNotNull = function(position, code, testValue) {
	if (testValue == null && stjs.assertHandler)
		stjs.assertHandler(position, code, "Wrong state. Null value");
}

stjs.STJSAssert.assertStateTrue = function(position, code, condition) {
	if (!condition && stjs.assertHandler)
		stjs.assertHandler(position, code, "Wrong state. Condition is false");
}
/** exception **/
var Throwable = function(message, cause){
	Error.call(this);
	if(typeof Error.captureStackTrace === 'function'){
		// nice way to capture the stack trace for chrome
		Error.captureStackTrace(this, arguments.callee);
	} else {
		// alternate way to capture the stack trace for other browsers
		try{
			throw new Error();
		}catch(e){
			this.stack = e.stack;
		}
	}
	if (typeof message === "string"){
		this.detailMessage  = message;
		this.message = message;
		this.cause = cause;
	} else {
		this.cause = message;
	}
};
stjs.extend(Throwable, Error, [], function(constructor, prototype){
	prototype.detailMessage = null;
	prototype.cause = null;
	prototype.getMessage = function() {
        return this.detailMessage;
    };

	prototype.getLocalizedMessage = function() {
        return this.getMessage();
    };

	prototype.getCause = function() {
        return (this.cause==this ? null : this.cause);
    };

	prototype.toString = function() {
	        var s = "Exception";//TODO should get the exception's type name here
	        var message = this.getLocalizedMessage();
	        return (message != null) ? (s + ": " + message) : s;
	 };

	 prototype.getStackTrace = function() {
		 return this.stack;
	 };

	 prototype.printStackTrace = function(){
		 console.error(this.getStackTrace());
	 };
}, {});

var Exception = function(message, cause){
	Throwable.call(this, message, cause);
};
stjs.extend(Exception, Throwable, [], function(constructor, prototype){
}, {});

var RuntimeException = function(message, cause){
	Exception.call(this, message, cause);
};
stjs.extend(RuntimeException, Exception, [], function(constructor, prototype){
}, {});

var Iterator = function() {};
Iterator = stjs.extend(Iterator, null, [], function(constructor, prototype) {
    prototype.hasNext = function() {};
    prototype.next = function() {};
    prototype.remove = function() {};
}, {}, {});

var Iterable = function() {};
Iterable = stjs.extend(Iterable, null, [], function(constructor, prototype) {
    prototype.iterator = function() {};
}, {}, {});

/** stjs field manipulation */
stjs.setField=function(obj, field, value, returnOldValue){
	if (stjs.setFieldHandler)
		return stjs.setFieldHandler(obj, field, value, returnOldValue);
	var toReturn = returnOldValue ? obj[field] : value;
	obj[field] = value;
	return toReturn;
};

stjs.getField=function(obj, field){
	if (stjs.getFieldHandler)
		return stjs.getFieldHandler(obj, field);
	return obj[field];
};


var live4api = {};

stjs.ns("live4api");
live4api.UserResponse = function(id, name, userpic, type, intoURL, homeTown, activities) {
    this.id = id;
    this.name = name;
    this.userpic = userpic;
    this.type = type;
    this.intoURL = intoURL;
    this.homeTown = homeTown;
    this.activities = activities;
};
live4api.UserResponse = stjs.extend(live4api.UserResponse, null, [], function(constructor, prototype) {
    prototype.id = null;
    prototype.name = null;
    prototype.userpic = null;
    prototype.type = null;
    prototype.intoURL = null;
    prototype.introUrl = null;
    prototype.homeTown = null;
    prototype.activities = null;
    prototype.activites = null;
}, {type: {name: "Enum", arguments: ["live4api.LoginType"]}, activities: {name: "Array", arguments: ["live4api.UserActivityResponse"]}, activites: {name: "Array", arguments: ["live4api.UserActivityResponse"]}}, {});
stjs.ns("live4api");
live4api.UserActivityResponse = function() {};
live4api.UserActivityResponse = stjs.extend(live4api.UserActivityResponse, null, [], function(constructor, prototype) {
    prototype.thumb = null;
    prototype.hashTags = null;
}, {hashTags: {name: "Array", arguments: [null]}}, {});
stjs.ns("live4api");
live4api.LoginType = stjs.enumeration("Facebook", "Twitter", "Email");
stjs.ns("live4api");
live4api.TimeInterval = function(startTime, endTime) {
    this.start = startTime;
    this.end = endTime;
};
live4api.TimeInterval = stjs.extend(live4api.TimeInterval, null, [], function(constructor, prototype) {
    prototype.start = null;
    prototype.end = null;
    prototype.contains = function(d) {
        var stime = this.start.getTime();
        var etime = this.end.getTime();
        var time = d.getTime();
        return stime <= time && time <= etime;
    };
    prototype.overlaps = function(that) {
        return this.contains(that.start) || this.contains(that.end);
    };
}, {start: "Date", end: "Date"}, {});
stjs.ns("live4api");
live4api.Dimension = function(w, h) {
    this.width = w;
    this.height = h;
};
live4api.Dimension = stjs.extend(live4api.Dimension, null, [], function(constructor, prototype) {
    prototype.width = 0;
    prototype.height = 0;
}, {}, {});
stjs.ns("live4api");
live4api.LikeResponse = function(likes, has_liked) {
    if (likes != null) {
        this.total_count = likes.length;
        this.likes = likes;
        this.has_liked = has_liked;
    } else {
        this.total_count = 0;
        this.likes = null;
        this.has_liked = false;
    }
};
live4api.LikeResponse = stjs.extend(live4api.LikeResponse, null, [], function(constructor, prototype) {
    prototype.total_count = 0;
    prototype.likes = null;
    prototype.can_like = true;
    prototype.has_liked = null;
}, {likes: {name: "Array", arguments: ["live4api.Like"]}}, {});
stjs.ns("live4api");
live4api.Like = function() {};
live4api.Like = stjs.extend(live4api.Like, null, [], function(constructor, prototype) {
    prototype.uuid = null;
    prototype.streamId = null;
    prototype.sid = null;
    prototype.user = null;
    prototype.startMsec = 0;
    prototype.ctime = 0;
    prototype.getId = function() {
        return this.uuid;
    };
    prototype.toString = function() {
        var userid = null;
        if (this.user != null) {
            userid = this.user.id;
        }
        return "<Like: streamId=" + this.streamId + "; from=" + userid + "; startMsec=" + this.startMsec + ">";
    };
}, {sid: "live4api.StreamId", user: "live4api.UserResponse"}, {});
stjs.ns("live4api");
live4api.MissionRole = stjs.enumeration("UNKNOWN", "PILOT", "PARTICIPANT", "OBSERVER", "OWNER");
stjs.ns("live4api");
live4api.UserRole = stjs.enumeration("USER", "ORG_ADMIN", "SUPER_ADMIN", "EXTERNAL");
var Internal = function() {};
Internal = stjs.extend(Internal, null, [], function(constructor, prototype) {
    constructor.mapValues = function(map) {
        var result = [];
        if (map == null) 
            return result;
        for (var k in map) {
            var item = map[k];
            result.push(item);
        }
        return result;
    };
    constructor.isBlank = function(string) {
        return string == null || "".equals(string) || string.matches("\\s+");
    };
    constructor.defaultMap = function(map) {
        return map == null ? {} : map;
    };
    constructor.containsKey = function(map, key) {
        return map != null && (map).hasOwnProperty(key);
    };
    constructor.defaultArray = function(arr) {
        return arr == null ? [] : arr;
    };
    constructor.defaultString = function(string, defaultString) {
        if (string != null && !"".equals(string)) {
            return string;
        }
        return defaultString;
    };
    constructor.isNotBlank = function(str) {
        return str != null && !"".equals(str) && !str.matches("\\s+");
    };
    constructor.isString = function(anything) {
        return "string".equals((typeof anything));
    };
    constructor.currentTimeMillis = function() {
        return stjs.trunc(Date.now());
    };
    constructor.isJava = "9007199254740993".equals("" + (9007199254740991 + 2));
    constructor.fromCharCode = function(charcode) {
        if (!Internal.isJava) {
            return (String).fromCharCode(charcode);
        } else {
            return String.valueOf(stjs.trunc(charcode));
        }
    };
    constructor.eq = function(str, str2) {
        if (str == null) {
            if (str2 == null) {
                return true;
            }
            return false;
        }
        return str.equals(str2);
    };
}, {}, {});
stjs.ns("live4api");
live4api.TSFile = function() {};
live4api.TSFile = stjs.extend(live4api.TSFile, null, [], function(constructor, prototype) {
    prototype.hashCode = function() {
        return this.filename == null ? -42 : this.filename.hashCode();
    };
    prototype.equals = function(obj) {
        if (!(stjs.isInstanceOf(obj.constructor, live4api.TSFile))) {
            return false;
        }
        var other = obj;
        if (this.filename == null) {
            return other.filename == null;
        } else {
            return this.filename.equals(other.filename);
        }
    };
    prototype.filename = null;
    prototype.filesize = 0;
    prototype.ctime = 0;
    prototype.startTime = 0;
    prototype.videoDuration = 0;
    prototype.timescale = 0;
    prototype.mseq = 0;
    prototype.getVideoDuration = function() {
        return this.videoDuration;
    };
    prototype.getVideoDurationMsec = function() {
        if (this.timescale != 0) {
            return stjs.trunc(this.getVideoDuration() * 1000 / this.timescale);
        } else {
            return 0;
        }
    };
    prototype.getVideoDurationSec = function() {
        if (this.timescale != 0) {
            return this.getVideoDuration() / this.timescale;
        } else {
            return 0.0;
        }
    };
    prototype.getMseq = function() {
        return this.mseq;
    };
    prototype.getFilename = function() {
        return this.filename;
    };
    prototype.getFilesize = function() {
        return this.filesize;
    };
    prototype.getStartTimeMsec = function() {
        if (this.timescale != 0) {
            return stjs.trunc(this.startTime * 1000 / this.timescale);
        }
        return 0;
    };
    prototype.getStartTime = function() {
        return this.startTime;
    };
    prototype.getCtime = function() {
        return this.ctime;
    };
    prototype.getTimescale = function() {
        return this.timescale;
    };
    prototype.setVideoDuration = function(videoDuration) {
        this.videoDuration = videoDuration;
    };
}, {}, {});
stjs.ns("live4api");
live4api.StreamId = function(userId, streamId) {
    this.userId = userId;
    this.streamId = streamId;
};
live4api.StreamId = stjs.extend(live4api.StreamId, null, [], function(constructor, prototype) {
    prototype.userId = null;
    prototype.streamId = null;
    prototype.toString = function() {
        return this.userId + "/" + this.streamId;
    };
    prototype._hashCode = 0;
    prototype.hashCode = function() {
        if (this._hashCode == 0) {
            this._hashCode = this.toString().hashCode();
        }
        return this._hashCode;
    };
    prototype.equals = function(obj) {
        if (obj != null) {
            return this.toString().equals(obj.toString());
        }
        return false;
    };
    constructor.sid = function(userId, streamId) {
        return new live4api.StreamId(userId, streamId);
    };
}, {}, {});
stjs.ns("live4api");
live4api.Api3CalendarUrls = function() {};
live4api.Api3CalendarUrls = stjs.extend(live4api.Api3CalendarUrls, null, [], function(constructor, prototype) {
    constructor.API_3_CALENDAR = "/api/3/calendar";
    constructor.OBJECT = "/object";
    constructor.LIST = "/list";
    constructor.createUrl = function() {
        return live4api.Api3CalendarUrls.API_3_CALENDAR + live4api.Api3CalendarUrls.OBJECT;
    };
    constructor.getUrl = function(id) {
        return live4api.Api3CalendarUrls.API_3_CALENDAR + live4api.Api3CalendarUrls.OBJECT + "/" + id;
    };
    constructor.listUrl = function(orgId) {
        return live4api.Api3CalendarUrls.API_3_CALENDAR + live4api.Api3CalendarUrls.LIST + "/" + orgId;
    };
}, {}, {});
stjs.ns("live4api");
live4api.Api1StreamUrls = function() {};
live4api.Api1StreamUrls = stjs.extend(live4api.Api1StreamUrls, null, [], function(constructor, prototype) {
    constructor.API_STREAM = "/api/stream";
    constructor.createUrl = function() {
        return live4api.Api1StreamUrls.API_STREAM;
    };
    constructor.listUrl = function() {
        return live4api.Api1StreamUrls.API_STREAM;
    };
    constructor.getUrl = function(id) {
        return live4api.Api1StreamUrls.API_STREAM + "/" + id;
    };
}, {}, {});
var GeocoderResult = function() {};
GeocoderResult = stjs.extend(GeocoderResult, null, [], function(constructor, prototype) {
    prototype.types = null;
    prototype.formattedAddress = null;
    prototype.addressComponents = null;
    prototype.geometry = null;
    prototype.partialMatch = false;
    prototype.getFormattedAddress = function() {
        return this.formattedAddress;
    };
    prototype.getGeometry = function() {
        return this.geometry;
    };
}, {types: {name: "Array", arguments: [null]}, addressComponents: {name: "Array", arguments: ["GeocoderAddressComponent"]}, geometry: "GeocoderGeometry"}, {});
stjs.ns("live4api");
live4api.LiveStatus = stjs.enumeration("SCHEDULED", "STANDBY", "LIVE", "UPLOADING", "UPLOADING_METADATA", "RECORDED");
stjs.ns("live4api");
live4api.Tag = function(id, name) {
    this.id = id;
    this.name = name;
    this.startMsec = 0;
    this.stopMsec = 0;
};
live4api.Tag = stjs.extend(live4api.Tag, null, [], function(constructor, prototype) {
    prototype.id = null;
    prototype.name = null;
    prototype.startMsec = 0;
    prototype.stopMsec = 0;
    prototype.toString = function() {
        return "Tag<" + this.id + "=" + this.name + ">";
    };
}, {}, {});
stjs.ns("live4api");
live4api.Doc = function() {};
live4api.Doc = stjs.extend(live4api.Doc, null, [], function(constructor, prototype) {
    prototype.getId = function() {};
    prototype.setId = function(id) {};
    prototype.isActive = function() {};
}, {}, {});
stjs.ns("live4api");
live4api.Privacy = stjs.enumeration("PUBLIC", "PRIVATE", "UNLISTED");
stjs.ns("live4api");
live4api.Api3Urls = function() {};
live4api.Api3Urls = stjs.extend(live4api.Api3Urls, null, [], function(constructor, prototype) {
    constructor.WSVIDEO = "/api/3/wsvideo";
    constructor.API_3_WSPUSHVIDEO = "/api/3/wspushvideo";
    constructor.API_3_ORG_STORAGE_UPLOAD = "/api/3/orgstorage/upload";
    constructor.API_3_ORG_STORAGE_GET = "/api/3/orgstorage/get";
    constructor.API_3_HWLOG = "/api/3/hwlog";
    constructor.API_3_LOGIN = "/api/3/login";
    constructor.API_3_RESETPASSWORD = "/api/3/resetpassword";
    constructor.API_3_WSUPDATES = "/api/3/wsupdates";
    constructor.API_3_LOCATIONS = "/api/3/locations";
    constructor.wsVideo = function(streamId) {
        return live4api.Api3Urls.WSVIDEO + "/" + streamId;
    };
    constructor.wsPushVideo = function(streamId) {
        return live4api.Api3Urls.API_3_WSPUSHVIDEO + "/" + streamId;
    };
    constructor.locationsUrl = function(streamId) {
        return live4api.Api3Urls.API_3_LOCATIONS + "/" + streamId;
    };
}, {}, {});
stjs.ns("live4api");
live4api.CommentResponse = function(comments) {
    if (comments != null) {
        this.total_count = comments.length;
        this.comments = comments;
    } else {
        this.total_count = 0;
        this.comments = null;
    }
};
live4api.CommentResponse = stjs.extend(live4api.CommentResponse, null, [], function(constructor, prototype) {
    prototype.total_count = 0;
    prototype.comments = null;
}, {comments: {name: "Array", arguments: ["live4api.Comment"]}}, {});
stjs.ns("live4api");
live4api.Api2Urls = function() {};
live4api.Api2Urls = stjs.extend(live4api.Api2Urls, null, [], function(constructor, prototype) {
    constructor.API_2_START = "/api/2/start";
    constructor.API_2_BEST = "/api/2/best";
    constructor.API_2_STREAM_UPDATE_TITLE = "/api/2/streamUpdateTitle";
    constructor.API_2_STREAM = "/api/2/stream";
    constructor.API_2_UPLOAD_LOCATION = "/api/2/upload/location";
    constructor.API_2_UPLOAD_LOG = "/api/2/upload/log";
    constructor.API_2_UPLOAD_AV = "/api/2/upload/av";
    constructor.API_2_NOTIFY_REGISTER = "/api/2/notify/register";
    constructor.API_2_MYSTREAMS = "/api/2/mystreams";
    constructor.API_2_CLOSE = "/api/2/close";
    constructor.API_2_END = "/api/2/end";
    constructor.API_2_USER = "/api/2/user";
    constructor.API_2_SEARCH = "/api/2/search";
    constructor.API_2_EXPLORE = "/api/2/explore";
    constructor.API_2_LOGIN = "/api/2/login";
    constructor.API_2_LIKE = "/api/2/like";
    constructor.API_2_ACTIVITY = "/api/2/activity";
    constructor.API_2_COMMENT = "/api/2/comment";
    constructor.API_2_REPORT = "/api/2/report";
    constructor.updateStreamTitleUrl = function(streamId) {
        return live4api.Api2Urls.API_2_STREAM_UPDATE_TITLE + "/" + streamId;
    };
}, {}, {});
var GeocoderGeometry = function() {};
GeocoderGeometry = stjs.extend(GeocoderGeometry, null, [], function(constructor, prototype) {
    prototype.location = null;
    prototype.locationType = null;
    prototype.viewport = null;
    prototype.bounds = null;
    prototype.getLocation = function() {
        return this.location;
    };
}, {location: "google.maps.LatLng", locationType: {name: "Enum", arguments: ["GeocoderLocationType"]}, viewport: "google.maps.LatLngBounds", bounds: "google.maps.LatLngBounds"}, {});
var GeocoderLocationType = stjs.enumeration("APPROXIMATE", "GEOMETRIC_CENTER", "RANGE_INTERPOLATED", "ROOFTOP", "UNKNOWN");
stjs.ns("live4api");
live4api.LoginRequestData = function(login, pass) {
    this.l = login;
    this.p = pass;
};
live4api.LoginRequestData = stjs.extend(live4api.LoginRequestData, null, [], function(constructor, prototype) {
    prototype.l = null;
    prototype.p = null;
    prototype.t = null;
}, {}, {});
stjs.ns("live4api");
live4api.BillingInfo = function() {};
live4api.BillingInfo = stjs.extend(live4api.BillingInfo, null, [], function(constructor, prototype) {
    prototype.account = null;
    prototype.conf = null;
    prototype.dataPlan = null;
    prototype.amount = null;
    prototype.card = null;
    prototype.information = null;
}, {}, {});
stjs.ns("live4api");
live4api.EndOfStream = function() {
    this.files = new Array();
};
live4api.EndOfStream = stjs.extend(live4api.EndOfStream, null, [], function(constructor, prototype) {
    prototype.files = null;
    constructor.ENDOFSTREAM_JS = "endofstream.js";
    constructor.ENDOFSTREAM_JS_GZ = "endofstream.js.gz";
}, {files: {name: "Array", arguments: ["live4api.CameraFile"]}}, {});
stjs.ns("live4api");
live4api.CameraFile = function(file, original) {
    this.file = file;
    this.originalName = original;
};
live4api.CameraFile = stjs.extend(live4api.CameraFile, null, [], function(constructor, prototype) {
    prototype.originalSize = null;
    prototype.originalName = null;
    prototype.size = null;
    prototype.file = null;
    prototype.lastModified = null;
    constructor.sortByFilename = function(h1, h2) {
        if (h1 != null && h2 != null) {
            return h1.file.compareTo(h2.file);
        }
        if (h1 == null && h2 == null) {
            return 0;
        }
        if (h1 != null) {
            return 1;
        }
        return -1;
    };
}, {sortByFilename: {name: "SortFunction", arguments: ["live4api.CameraFile"]}}, {});
stjs.ns("live4api");
live4api.UserProfile = function() {};
live4api.UserProfile = stjs.extend(live4api.UserProfile, null, [], function(constructor, prototype) {
    prototype.department = null;
    prototype.title = null;
    prototype.phone = null;
    prototype.notes = null;
    prototype.role = null;
    prototype.active = false;
}, {role: {name: "Enum", arguments: ["live4api.UserRole"]}}, {});
stjs.ns("live4api");
live4api.Api3HwUrls = function() {};
live4api.Api3HwUrls = stjs.extend(live4api.Api3HwUrls, null, [], function(constructor, prototype) {
    constructor.API_3_HW = "/api/3/hardware";
    constructor.FIND_BY_PORT = "/findByPort";
    constructor.RELEASE = "/release";
    constructor.OBJECT = "/object";
    constructor.LIST = "/list";
    constructor.createUrl = function() {
        return live4api.Api3HwUrls.API_3_HW + live4api.Api3HwUrls.OBJECT;
    };
    constructor.getUrl = function(id) {
        return live4api.Api3HwUrls.API_3_HW + live4api.Api3HwUrls.OBJECT + "/" + id;
    };
    constructor.listUrl = function(orgId) {
        return live4api.Api3HwUrls.API_3_HW + live4api.Api3HwUrls.LIST + "/" + orgId;
    };
    constructor.findByPortUrl = function(port) {
        return live4api.Api3HwUrls.API_3_HW + live4api.Api3HwUrls.FIND_BY_PORT + "/" + port;
    };
    constructor.releaseUrl = function(id) {
        return live4api.Api3HwUrls.API_3_HW + live4api.Api3HwUrls.RELEASE + "/" + id;
    };
}, {}, {});
var GeocoderAddressComponent = function() {};
GeocoderAddressComponent = stjs.extend(GeocoderAddressComponent, null, [], function(constructor, prototype) {
    prototype.longName = null;
    prototype.shortName = null;
    prototype.types = null;
}, {types: {name: "Array", arguments: [null]}}, {});
stjs.ns("live4api");
live4api.Api3UserUrls = function() {};
live4api.Api3UserUrls = stjs.extend(live4api.Api3UserUrls, null, [], function(constructor, prototype) {
    constructor.API_3_USER = "/api/3/users";
    constructor.BYEMAIL = "/byemail";
    constructor.OBJECT = "/object";
    constructor.LIST = "/list";
    constructor.CHECK = "/check";
    constructor.ISTEMP = "/istemp";
    constructor.createUrl = function() {
        return live4api.Api3UserUrls.API_3_USER + live4api.Api3UserUrls.OBJECT;
    };
    constructor.updateUrl = function() {
        return live4api.Api3UserUrls.API_3_USER + live4api.Api3UserUrls.OBJECT;
    };
    constructor.getUrl = function(id) {
        return live4api.Api3UserUrls.API_3_USER + live4api.Api3UserUrls.OBJECT + "/" + id;
    };
    constructor.listUrl = function(orgId) {
        return live4api.Api3UserUrls.API_3_USER + live4api.Api3UserUrls.LIST + "/" + orgId;
    };
    constructor.byEmailUrl = function(email) {
        return live4api.Api3UserUrls.API_3_USER + live4api.Api3UserUrls.BYEMAIL + "/" + email;
    };
    constructor.checkUserByEmail = function(email) {
        return live4api.Api3UserUrls.API_3_USER + live4api.Api3UserUrls.CHECK + "/" + email;
    };
    constructor.isUserTemp = function(email) {
        return live4api.Api3UserUrls.API_3_USER + live4api.Api3UserUrls.ISTEMP + "/" + email;
    };
}, {}, {});
stjs.ns("live4api");
live4api.Api3StreamUrls = function() {};
live4api.Api3StreamUrls = stjs.extend(live4api.Api3StreamUrls, null, [], function(constructor, prototype) {
    constructor.API_3_STREAM = "/api/3/stream";
    constructor.OBJECT = "/object";
    constructor.LIST = "/list";
    constructor.createUrl = function() {
        return live4api.Api3StreamUrls.API_3_STREAM + live4api.Api3StreamUrls.OBJECT;
    };
    constructor.getUrl = function(id) {
        return live4api.Api3StreamUrls.API_3_STREAM + live4api.Api3StreamUrls.OBJECT + "/" + id;
    };
    constructor.updateUrl = function() {
        return live4api.Api3StreamUrls.API_3_STREAM + live4api.Api3StreamUrls.OBJECT;
    };
}, {}, {});
stjs.ns("live4api");
live4api.Api3OrgUrls = function() {};
live4api.Api3OrgUrls = stjs.extend(live4api.Api3OrgUrls, null, [], function(constructor, prototype) {
    constructor.API_3_ORG = "/api/3/org";
    constructor.CREATEWITHADMIN = "/createWithAdmin";
    constructor.OBJECT = "/object";
    constructor.LIST = "/list";
    constructor.baseUrl = function() {
        return live4api.Api3OrgUrls.API_3_ORG + live4api.Api3OrgUrls.OBJECT;
    };
    constructor.createUrl = function() {
        return live4api.Api3OrgUrls.API_3_ORG + live4api.Api3OrgUrls.OBJECT;
    };
    constructor.getUrl = function(orgId) {
        return live4api.Api3OrgUrls.API_3_ORG + live4api.Api3OrgUrls.OBJECT + "/" + orgId;
    };
    constructor.listUrl = function(orgId) {
        return live4api.Api3OrgUrls.API_3_ORG + live4api.Api3OrgUrls.LIST + "/" + orgId;
    };
    constructor.createWithAdminUrl = function() {
        return live4api.Api3OrgUrls.API_3_ORG + live4api.Api3OrgUrls.CREATEWITHADMIN;
    };
}, {}, {});
var ReadWrite = function() {};
ReadWrite = stjs.extend(ReadWrite, null, [], null, {}, {});
var TwilioToken = function() {};
TwilioToken = stjs.extend(TwilioToken, null, [], function(constructor, prototype) {
    prototype.identity = null;
    prototype.token = null;
}, {}, {});
stjs.ns("live4api");
live4api.AccessToken = function(token, secret, expires) {
    this.access_token = token;
    this.secret = secret;
    this.expires = expires;
};
live4api.AccessToken = stjs.extend(live4api.AccessToken, null, [], function(constructor, prototype) {
    prototype.secret = null;
    prototype.access_token = null;
    prototype.expires = 0;
}, {}, {});
stjs.ns("live4api");
live4api.Api3MissionUrls = function() {};
live4api.Api3MissionUrls = stjs.extend(live4api.Api3MissionUrls, null, [], function(constructor, prototype) {
    constructor.API_3_MISSION = "/api/3/mission";
    constructor.TOKEN = "/token";
    constructor.SHARE = "/share";
    constructor.JOIN = "Join";
    constructor.UNSHARE = "/unshare";
    constructor.CANCEL_NOTIFICATION = "/cancelNotification";
    constructor.INVITE = "/invite";
    constructor.OBJECT = "/object";
    constructor.LIST = "/list";
    constructor._MISSIONS = "/missions";
    constructor.MISSION_SHARE_PARAM = "t";
    constructor.CHAT_TOKEN = "/chatToken";
    constructor.AUDIO_CHAT_TOKEN = "/audioChatToken";
    constructor.BY_MISSION_TOKEN = "/byMissionToken";
    constructor.IS_TOKEN_VALID = "/isTokenValid";
    constructor.baseUrl = function() {
        return live4api.Api3MissionUrls.API_3_MISSION + live4api.Api3MissionUrls.OBJECT;
    };
    constructor.createUrl = function() {
        return live4api.Api3MissionUrls.API_3_MISSION + live4api.Api3MissionUrls.OBJECT;
    };
    constructor.updateUrl = function() {
        return live4api.Api3MissionUrls.API_3_MISSION + live4api.Api3MissionUrls.OBJECT;
    };
    constructor.getUrl = function(id) {
        return live4api.Api3MissionUrls.API_3_MISSION + live4api.Api3MissionUrls.OBJECT + "/" + id;
    };
    constructor.listUrl = function(orgId) {
        return live4api.Api3MissionUrls.API_3_MISSION + live4api.Api3MissionUrls.LIST + "/" + orgId;
    };
    constructor.tokenUrl = function(missionId) {
        return live4api.Api3MissionUrls.API_3_MISSION + live4api.Api3MissionUrls.TOKEN + "/" + missionId;
    };
    constructor.shareUrl = function(missionId) {
        return live4api.Api3MissionUrls.API_3_MISSION + live4api.Api3MissionUrls.SHARE + "/" + missionId;
    };
    constructor.unshareUrl = function(missionId) {
        return live4api.Api3MissionUrls.API_3_MISSION + live4api.Api3MissionUrls.UNSHARE + "/" + missionId;
    };
    constructor.joinUrl = function(missionId) {
        return live4api.Api3MissionUrls.API_3_MISSION + live4api.Api3MissionUrls.JOIN + "/" + missionId;
    };
    constructor.baseJoinUrl = function() {
        return live4api.Api3MissionUrls.API_3_MISSION + live4api.Api3MissionUrls.JOIN;
    };
    constructor.cancelNotificationUrl = function(missionId) {
        return live4api.Api3MissionUrls.API_3_MISSION + live4api.Api3MissionUrls.CANCEL_NOTIFICATION + "/" + missionId;
    };
    constructor.inviteUrl = function(missionId) {
        return live4api.Api3MissionUrls.API_3_MISSION + live4api.Api3MissionUrls.INVITE + "/" + missionId;
    };
    constructor.shareMissionUrl = function(missionId, shareToken) {
        return live4api.Api3MissionUrls._MISSIONS + "/" + missionId + "?" + live4api.Api3MissionUrls.MISSION_SHARE_PARAM + "=" + shareToken;
    };
    constructor.getUserByMissionToken = function(token) {
        return live4api.Api3MissionUrls.API_3_MISSION + live4api.Api3MissionUrls.BY_MISSION_TOKEN + "/" + token;
    };
    constructor.checkTokenUrl = function(token) {
        return live4api.Api3MissionUrls.API_3_MISSION + live4api.Api3MissionUrls.IS_TOKEN_VALID + "/" + token;
    };
}, {}, {});
stjs.ns("live4api");
live4api.DataSegment = function(playerTime, l) {
    this.playerTime = playerTime;
    this.location = l;
};
live4api.DataSegment = stjs.extend(live4api.DataSegment, null, [], function(constructor, prototype) {
    prototype.playerTime = 0;
    prototype.location = null;
    prototype.nearBy = null;
    prototype.width = 0;
    prototype.left = 0;
    prototype.isEmpty = false;
    prototype.widthScaled = 0;
    prototype.leftScaled = 0;
    prototype.tsfile = null;
    prototype.descr = null;
    prototype.getTime = function() {
        return this.left;
    };
    prototype.setWidth = function(width) {
        this.width = width;
    };
    prototype.setLeft = function(left) {
        this.left = left;
    };
    prototype.toString = function() {
        return String.format("%b; w%d; l%d p%d", this.isEmpty, this.width, this.left, this.playerTime);
    };
    prototype.scale = function(i) {
        this.widthScaled = Math.max(stjs.trunc(this.width / i), 1);
        this.leftScaled = stjs.trunc(this.left / i);
    };
}, {location: "live4api.StreamLocation", tsfile: "live4api.TSFile"}, {});
var ReadOnly = function() {};
ReadOnly = stjs.extend(ReadOnly, null, [], null, {}, {});
stjs.ns("live4api");
live4api.LiveMessage = function() {};
live4api.LiveMessage = stjs.extend(live4api.LiveMessage, null, [], function(constructor, prototype) {
    prototype.streamId = null;
    prototype.stream = null;
    prototype.nearby = null;
    prototype.durationHLS = null;
    prototype.durationDash = null;
    prototype.map = null;
    prototype.action = null;
    prototype.calendar = null;
    prototype.org = null;
    prototype.hardware = null;
    prototype.mission = null;
    prototype.user = null;
    prototype.hasMap = function() {
        return this.map != null;
    };
    prototype.hasDuration = function() {
        return this.durationDash != null || this.durationHLS != null;
    };
    prototype.hasNearby = function() {
        return this.nearby != null;
    };
    prototype.hasStream = function() {
        return this.stream != null;
    };
    constructor.subscribeStream = function(sid) {
        var tm = new live4api.LiveMessage();
        tm.action = "stream/subscribe";
        tm.streamId = sid;
        return tm;
    };
    constructor.unsubscribeStream = function(sid) {
        var tm = new live4api.LiveMessage();
        tm.action = "stream/unsubscribe";
        tm.streamId = sid;
        return tm;
    };
    constructor.subscribe = function(what) {
        var tm = new live4api.LiveMessage();
        tm.action = what + "/subscribe";
        return tm;
    };
}, {stream: "live4api.StreamResponse", nearby: {name: "Array", arguments: ["live4api.DataSegment"]}, map: {name: "Array", arguments: ["live4api.DataSegment"]}, calendar: "live4api.Calendar", org: "live4api.Organization", hardware: "live4api.Hardware", mission: "live4api.Mission", user: "live4api.User"}, {});
stjs.ns("live4api");
live4api.Comment = function() {};
live4api.Comment = stjs.extend(live4api.Comment, null, [], function(constructor, prototype) {
    prototype.uuid = null;
    prototype.sid = null;
    prototype.streamId = null;
    prototype.user = null;
    prototype.body = null;
    prototype.startMsec = 0;
    prototype.stopMsec = 0;
    prototype.ctime = 0;
    prototype.mtime = 0;
    prototype.getId = function() {
        return this.uuid;
    };
    prototype.getBody = function() {
        return Internal.defaultString(this.body, "");
    };
    prototype.setBody = function(body) {
        this.body = body;
        this.mtime = Internal.currentTimeMillis();
    };
}, {sid: "live4api.StreamId", user: "live4api.UserResponse"}, {});
stjs.ns("live4api");
live4api.StreamLocation = function(timestamp) {
    this.timestamp = timestamp;
};
live4api.StreamLocation = stjs.extend(live4api.StreamLocation, null, [], function(constructor, prototype) {
    constructor.speedLocation = function(timestamp, speed) {
        var l = new live4api.StreamLocation(timestamp);
        l.speed = speed;
        return l;
    };
    constructor.latLng = function(timestamp, latitude, longitude) {
        var l = new live4api.StreamLocation(timestamp);
        l.latitude = latitude;
        l.longitude = longitude;
        return l;
    };
    constructor.sortByTime = function(h1, h2) {
        if (h1 != null && h2 != null) {
            return stjs.trunc((h1.getTime() - h2.getTime()));
        }
        if (h1 == null && h2 == null) {
            return 0;
        }
        if (h1 != null) {
            return 1;
        }
        return -1;
    };
    prototype.hashCode = function() {
        return this.timestamp == null ? -42 : this.timestamp.hashCode();
    };
    prototype.altitude = null;
    prototype.course = null;
    prototype.horizontalAccuracy = null;
    prototype.latitude = null;
    prototype.longitude = null;
    prototype.speed = null;
    prototype.timestamp = null;
    prototype.verticalAccuracy = null;
    prototype.playerTime = null;
    prototype.streamId = null;
    prototype.getSpeed = function() {
        return this.speed;
    };
    prototype.getTimestamp = function() {
        return this.timestamp;
    };
    prototype.timeMsec = 0;
    constructor.accurateLocations = function(l) {
        return l.speed >= 5 || l.horizontalAccuracy <= 20;
    };
    prototype.getTime = function() {
        if (this.timeMsec == 0) {
            if (Internal.isJava) {
                this.timeMsec = Internal.tryParseDate(this.timestamp);
            } else {
                return stjs.trunc(new Date(this.timestamp).getTime());
            }
        }
        return this.timeMsec;
    };
    prototype.lalo = function() {
        return this.latitude + "," + this.longitude;
    };
}, {sortByTime: {name: "SortFunction", arguments: ["live4api.StreamLocation"]}, accurateLocations: {name: "Function1", arguments: ["live4api.StreamLocation", null]}}, {});
stjs.ns("live4api");
live4api.Address = function() {};
live4api.Address = stjs.extend(live4api.Address, null, [], function(constructor, prototype) {
    prototype.line1 = null;
    prototype.line2 = null;
    prototype.city = null;
    prototype.state = null;
    prototype.zip = null;
    prototype.country = null;
    prototype.asOneLine = function() {
        var state_zip = Internal.defaultString(this.state, "") + " " + Internal.defaultString(this.zip, "");
        return [this.line1, this.line2, this.city, state_zip, this.country].filter(function(s, aLong, strings) {
            return Internal.isNotBlank(s);
        }).join(",");
    };
}, {}, {});
stjs.ns("live4api");
live4api.Mission = function() {};
live4api.Mission = stjs.extend(live4api.Mission, null, [live4api.Doc], function(constructor, prototype) {
    constructor.State = stjs.enumeration("PENDING", "STARTED", "CANCELLED", "ENDED");
    constructor.ShareToken = function() {};
    constructor.ShareToken = stjs.extend(constructor.ShareToken, null, [], function(constructor, prototype) {
        prototype.token = null;
        prototype.missionId = null;
        prototype.userId = null;
        prototype.invitedId = null;
    }, {}, {});
    prototype._rev = 0;
    prototype.id = null;
    prototype.createdByUserId = null;
    prototype.orgId = null;
    prototype.mtime = 0;
    prototype.name = null;
    prototype.location = null;
    prototype.startTime = null;
    prototype.endTime = null;
    prototype.timeZone = null;
    prototype.streamIds = null;
    prototype.roles = null;
    prototype.pilots = null;
    prototype.joined = null;
    prototype.hardware = null;
    prototype.state = null;
    constructor.UNASSIGNED = "Unassigned";
    prototype.getId = function() {
        return this.id;
    };
    prototype.isActive = function() {
        return this.state == live4api.Mission.State.PENDING || this.state == live4api.Mission.State.STARTED;
    };
    prototype.setId = function(id) {
        this.id = id;
    };
    prototype.addStream = function(streamId) {
        this.streamIds = Internal.defaultArray(this.streamIds);
        this.streamIds.push(streamId);
    };
    prototype.hasStreamId = function(streamId) {
        return Internal.defaultArray(this.streamIds).some(function(_m, i, a) {
            return _m.equals(streamId);
        });
    };
    prototype.hasOwnerPermissions = function(u) {
        return live4api.Mission.isOrgAdmin(u, this) || live4api.Mission.isOwner(u, this);
    };
    prototype.hasPilotPermissions = function(u) {
        return live4api.MissionRole.PILOT == Internal.defaultMap(this.roles)[u.id] || this.hasOwnerPermissions(u);
    };
    prototype.hasParticipantPermisisons = function(u) {
        return this.hasPilotPermissions(u) || live4api.MissionRole.PARTICIPANT == Internal.defaultMap(this.roles)[u.id];
    };
    prototype.removeUser = function(userId) {
        delete Internal.defaultMap(this.roles)[userId];
    };
    prototype.addUser = function(user, role) {
        this.roles = Internal.defaultMap(this.roles);
        this.roles[user.id] = role;
    };
    prototype.addPilot = function(streamId, pilot) {
        this.pilots = Internal.defaultMap(this.pilots);
        this.pilots[streamId] = pilot.id;
    };
    prototype.removePilot = function(streamId) {
        this.pilots = Internal.defaultMap(this.pilots);
        delete this.pilots[streamId];
    };
    prototype.getPilotId = function(streamId) {
        return Internal.defaultMap(this.pilots)[streamId];
    };
    prototype.changeMissionName = function(newMissionName) {
        this.name = newMissionName;
    };
    prototype.hasUser = function(id) {
        return Internal.containsKey(this.roles, id);
    };
    prototype.getOwnerId = function() {
        for (var userId in Internal.defaultMap(this.roles)) {
            var missionRole = this.roles[userId];
            if (live4api.MissionRole.OWNER == missionRole) {
                return userId;
            }
        }
        return this.createdByUserId;
    };
    prototype.hasOwnerRole = function() {
        for (var userId in Internal.defaultMap(this.roles)) {
            var missionRole = this.roles[userId];
            if (live4api.MissionRole.OWNER == missionRole) {
                return true;
            }
        }
        return false;
    };
    prototype.countOwners = function() {
        var count = 0;
        for (var userId in Internal.defaultMap(this.roles)) {
            var missionRole = this.roles[userId];
            if (live4api.MissionRole.OWNER == missionRole) {
                count += 1;
            }
        }
        return count;
    };
    prototype.isLive = function() {
        return live4api.Mission.State.STARTED.equals(this.state);
    };
    prototype.isScheduled = function() {
        return live4api.Mission.State.PENDING.equals(this.state);
    };
    prototype.isCompleted = function() {
        return live4api.Mission.State.ENDED.equals(this.state);
    };
    prototype.addHardware = function(h) {
        var hw = Internal.defaultArray(this.hardware);
        var found = false;
        for (var i = 0; i < hw.length; i++) {
            if (hw[i].id.equals(h.id)) {
                found = true;
                hw[i] = h;
            }
        }
        if (!found) {
            hw.push(h);
        }
        this.hardware = hw;
    };
    prototype.getTimeInterval = function() {
        return new live4api.TimeInterval(this.startTime, this.endTime);
    };
    prototype.isRunningNow = function() {
        return this.getTimeInterval().contains(new Date());
    };
    constructor.isOrgAdmin = function(u, m) {
        return u.isOrgAdmin(m.orgId);
    };
    constructor.isOwner = function(u, m) {
        return live4api.MissionRole.OWNER == Internal.defaultMap(m.roles)[u.id];
    };
    constructor.isScheduler = function(u, m) {
        return Internal.eq(m.createdByUserId, u.id);
    };
}, {startTime: "Date", endTime: "Date", streamIds: {name: "Array", arguments: [null]}, roles: {name: "Map", arguments: [null, {name: "Enum", arguments: ["live4api.MissionRole"]}]}, pilots: {name: "Map", arguments: [null, null]}, joined: {name: "Map", arguments: [null, "Date"]}, hardware: {name: "Array", arguments: ["live4api.Hardware"]}, state: {name: "Enum", arguments: ["live4api.Mission.State"]}}, {});
stjs.ns("live4api");
live4api.Calendar = function() {};
live4api.Calendar = stjs.extend(live4api.Calendar, null, [live4api.Doc], function(constructor, prototype) {
    prototype._rev = 0;
    prototype.id = null;
    prototype.intervals = null;
    prototype.getId = function() {
        return this.id;
    };
    prototype.setId = function(id) {
        this.id = id;
    };
    prototype.isActive = function() {
        return true;
    };
    prototype.isBusyAt = function(interval) {
        if (interval == null) {
            return false;
        }
        return Internal.mapValues(this.intervals).some(function(ti, i, a) {
            return ti != null && ti.overlaps(interval);
        });
    };
}, {intervals: {name: "Map", arguments: [null, "live4api.TimeInterval"]}}, {});
stjs.ns("live4api");
live4api.Hardware = function(name, type) {
    this.name = name;
    this.type = type;
    this.active = true;
};
live4api.Hardware = stjs.extend(live4api.Hardware, null, [live4api.Doc], function(constructor, prototype) {
    constructor.TYPE_MC_BOX = "MC_BOX";
    constructor.TYPE_DRONE = "DRONE";
    constructor.TYPE_ANDROID = "ANDROID";
    prototype.id = null;
    prototype._rev = 0;
    prototype.name = null;
    prototype.type = null;
    prototype.manufacturer = null;
    prototype.model = null;
    prototype.orgId = null;
    prototype.active = false;
    prototype.port = 0;
    prototype.externalId = null;
    prototype.endpoint = null;
    constructor.sortByNameAvailableFirst = function(h1, h2) {
        var diff = live4api.Hardware.statusLabel(h1._availability).compareTo(live4api.Hardware.statusLabel(h2._availability));
        return diff != 0 ? diff : h1.name.compareTo(h2.name);
    };
    prototype._availability = null;
    prototype._calendar = null;
    prototype._orgName = null;
    constructor.isValidPortNumber = function(port) {
        return port > 1024 && port < 65536;
    };
    prototype.isMCBox = function() {
        return this.type.equals(live4api.Hardware.TYPE_MC_BOX);
    };
    prototype.isDrone = function() {
        return this.type.equals(live4api.Hardware.TYPE_DRONE);
    };
    constructor.MCBox = function(name) {
        return new live4api.Hardware(name, live4api.Hardware.TYPE_MC_BOX);
    };
    constructor.drone = function(name) {
        return new live4api.Hardware(name, live4api.Hardware.TYPE_DRONE);
    };
    constructor.android = function(name) {
        return new live4api.Hardware(name, live4api.Hardware.TYPE_ANDROID);
    };
    prototype.isAvailable = function() {
        return !this.isScheduled();
    };
    prototype.isScheduled = function() {
        return this._availability == live4api.Hardware.Availability.SCHEDULED || this._availability == live4api.Hardware.Availability.INUSE;
    };
    prototype.setPort = function(port) {
        this.port = port;
        return this;
    };
    prototype.belongsToOrg = function(orgId) {
        return !Internal.isBlank(orgId) && orgId.equals(this.orgId);
    };
    constructor.Availability = stjs.enumeration("AVAILABLE", "SCHEDULED", "INUSE");
    prototype.isAssigned = function() {
        return this.orgId != null;
    };
    constructor.statusLabel = function(s) {
        if (s == live4api.Hardware.Availability.AVAILABLE) {
            return "Available";
        } else if (s == live4api.Hardware.Availability.SCHEDULED) {
            return "Scheduled";
        } else if (s == live4api.Hardware.Availability.INUSE) {
            return "In use";
        }
        return "Unknown";
    };
    prototype.getId = function() {
        return this.id;
    };
    prototype.isActive = function() {
        return this.active;
    };
    prototype.setId = function(id) {
        this.id = id;
    };
    prototype.getAvailabilityFor = function(ti) {
        if (this._calendar == null) 
            return live4api.Hardware.Availability.AVAILABLE;
        return this._calendar.isBusyAt(ti) ? live4api.Hardware.Availability.SCHEDULED : live4api.Hardware.Availability.AVAILABLE;
    };
}, {sortByNameAvailableFirst: {name: "SortFunction", arguments: ["live4api.Hardware"]}, _availability: {name: "Enum", arguments: ["live4api.Hardware.Availability"]}, _calendar: "live4api.Calendar"}, {});
stjs.ns("live4api");
live4api.Stream = function() {};
live4api.Stream = stjs.extend(live4api.Stream, null, [live4api.Doc], function(constructor, prototype) {
    prototype._rev = 0;
    constructor.createStream = function(sid, privacy) {
        var s = new live4api.Stream();
        s.filename = sid.streamId;
        s.userId = sid.userId;
        s.privacy = privacy;
        s.ctime = stjs.trunc(Date.now());
        return s;
    };
    prototype.userId = null;
    prototype.filename = null;
    prototype.startAddress = null;
    prototype.startGeoCoder = null;
    prototype.startLocation = null;
    prototype.onCdn = false;
    prototype.onYoutube = null;
    prototype.mtime = 0;
    prototype.ctime = 0;
    prototype.status = null;
    prototype.views = 0;
    prototype.startTimeMsec = 0;
    prototype.title = null;
    prototype.privacy = null;
    prototype.locationHidden = false;
    prototype.tags2 = null;
    prototype.avgSpeed = 0;
    prototype.maxSpeed = 0;
    prototype.maxAlt = 0;
    prototype.width = -1;
    prototype.height = -1;
    prototype.hardwareId = null;
    prototype.liveCodecs = null;
    prototype.closed = false;
    prototype.m3u8 = null;
    prototype.mpd = null;
    prototype.dash = null;
    prototype.webm = null;
    prototype.mp4 = null;
    prototype.thumb = null;
    prototype.md = null;
    prototype.sid = function() {
        return new live4api.StreamId(this.userId, this.filename);
    };
    prototype.isLive = function() {
        return this.getStatus() == live4api.LiveStatus.LIVE;
    };
    prototype.isScheduled = function() {
        return this.getStatus() == live4api.LiveStatus.SCHEDULED || this.getStatus() == live4api.LiveStatus.STANDBY;
    };
    prototype.isUploading = function() {
        var _status = this.getStatus();
        return _status == live4api.LiveStatus.UPLOADING || _status == live4api.LiveStatus.UPLOADING_METADATA;
    };
    prototype.isRecorded = function() {
        return this.getStatus() == live4api.LiveStatus.RECORDED;
    };
    prototype.getPrivacy = function() {
        if (this.privacy == null) {
            this.privacy = live4api.Privacy.PUBLIC;
        }
        return this.privacy;
    };
    prototype.setPrivacy = function(privacy) {
        this.privacy = privacy;
    };
    prototype.safeStreamId = function() {
        return this.userId + "_" + this.filename.replace(".", "");
    };
    prototype.getStatus = function() {
        return this.status;
    };
    prototype.setStatus = function(status) {
        this.status = status;
    };
    prototype.isClosed = function() {
        return this.closed;
    };
    prototype.setClosed = function(closed) {
        this.closed = closed;
    };
    prototype.getMp4 = function() {
        return this.mp4;
    };
    prototype.getThumb = function() {
        return this.thumb;
    };
    prototype.getM3u8 = function() {
        return this.m3u8;
    };
    prototype.getId = function() {
        return this.sid().toString();
    };
    prototype.setId = function(id) {
        var split = (id.split("/"));
        this.userId = split[0];
        this.filename = split[1];
    };
    prototype.isActive = function() {
        return true;
    };
}, {startGeoCoder: {name: "Array", arguments: ["GeocoderResult"]}, startLocation: "live4api.StreamLocation", status: {name: "Enum", arguments: ["live4api.LiveStatus"]}, privacy: {name: "Enum", arguments: ["live4api.Privacy"]}, tags2: {name: "Array", arguments: ["live4api.Tag"]}}, {});
stjs.ns("live4api");
live4api.StreamPermissions = function() {};
live4api.StreamPermissions = stjs.extend(live4api.StreamPermissions, null, [], function(constructor, prototype) {
    constructor.canUpdateStreamById = function(sid, user) {
        if (sid == null || user == null) {
            return false;
        }
        return live4api.StreamPermissions.userOwnsStream(sid, user);
    };
    constructor.userOwnsStream = function(sid, user) {
        return user.isSuperAdmin() || sid.userId.equals(user.getId());
    };
    constructor.canGetStreamById = function(sid, user) {
        if (sid == null || user == null) {
            return false;
        }
        return live4api.StreamPermissions.userOwnsStream(sid, user);
    };
    constructor.canGetStream = function(stream, user) {
        if (stream == null || user == null) {
            return false;
        }
        return live4api.StreamPermissions.userOwnsStream(stream.sid(), user) || !live4api.Privacy.PRIVATE.equals(stream.getPrivacy());
    };
}, {}, {});
stjs.ns("live4api");
live4api.User = function(id, name, userpic, created, social, email) {
    this.id = id;
    this.name = name;
    this._created = created;
    this.userpic = userpic;
    this.type = social;
    this.email = email;
    this.profiles = {};
};
live4api.User = stjs.extend(live4api.User, null, [live4api.Doc], function(constructor, prototype) {
    prototype._rev = 0;
    prototype.id = null;
    prototype.name = null;
    prototype.lastname = null;
    prototype._created = 0;
    prototype.userpic = null;
    prototype.email = null;
    prototype.type = null;
    prototype.password = null;
    prototype.session = null;
    prototype.emailVerified = false;
    prototype.resetPasswordToken = null;
    prototype.tokenExpireTime = null;
    prototype.licenseAgreementAccepted = false;
    prototype.sudo = false;
    prototype.profiles = null;
    prototype.getId = function() {
        return this.id;
    };
    prototype.setId = function(id) {
        this.id = id;
    };
    prototype.isActive = function() {
        return this.isUserActiveInAnyOrg();
    };
    prototype.isUserActiveInOrg = function(orgId) {
        var userProfile = this.getProfile(orgId);
        return userProfile != null && userProfile.active;
    };
    prototype.isUserActiveInAnyOrg = function() {
        for (var orgId in this.profiles) {
            if (this.getProfile(orgId).active) {
                return true;
            }
        }
        return false;
    };
    prototype.belongsToOrg = function(orgId) {
        var profile = this.getProfile(orgId);
        return profile != null;
    };
    prototype.hasCommonOrg = function(checkUser) {
        if (checkUser != null) {
            for (var orgId in this.profiles) {
                if (checkUser.belongsToOrg(orgId)) {
                    return true;
                }
            }
        }
        return false;
    };
    prototype.getName = function() {
        return (this.name || this.id);
    };
    prototype.getFullName = function() {
        return (this.name || "") + " " + (this.lastname || "");
    };
    prototype.getAvatarUrl = function() {
        return this.userpic;
    };
    prototype.getStatusString = function(orgId) {
        var userProfile = this.getProfile(orgId);
        return userProfile == null ? null : userProfile.active ? "Active" : "Inactive";
    };
    prototype.getEmail = function() {
        return this.email;
    };
    prototype.created = function() {
        return this._created;
    };
    prototype.setAccessToken = function(token) {
        this.session = token;
    };
    prototype.getType = function() {
        return this.type;
    };
    prototype.getRole = function(orgId) {
        var profile = this.getProfile(orgId);
        if (profile == null) {
            return this.isSuperAdmin() ? live4api.UserRole.SUPER_ADMIN : null;
        } else {
            return profile.role;
        }
    };
    prototype.passwordMatches = function(password) {
        if (this.password == null) {
            return password == null;
        } else {
            return this.password.equals(password);
        }
    };
    prototype.toString = function() {
        return this.id + "@" + this.getType();
    };
    prototype.setPassword = function(password) {
        this.password = password;
    };
    prototype.setAvatarUrl = function(url) {
        this.userpic = url;
    };
    prototype.isExternal = function(orgId) {
        var profile = this.getProfile(orgId);
        return profile == null || live4api.UserRole.EXTERNAL.equals(profile.role);
    };
    prototype.isSuperAdmin = function() {
        return this.sudo;
    };
    prototype.isOrgAdmin = function(orgId) {
        return this.getRole(orgId) == live4api.UserRole.ORG_ADMIN;
    };
    prototype.isOrgAdminInAnyOrg = function() {
        for (var orgId in this.profiles) {
            if (this.getRole(orgId).equals(live4api.UserRole.ORG_ADMIN)) {
                return true;
            }
        }
        return false;
    };
    prototype.getFirstOrgId = function() {
        for (var orgId in Internal.defaultMap(this.profiles)) {
            var profile = this.getProfile(orgId);
            if (profile != null && profile.active) {
                return orgId;
            }
        }
        return null;
    };
    prototype.setRole = function(orgId, role) {
        this.createProfile(orgId);
        this.getProfile(orgId).role = role;
    };
    prototype.setSuperAdmin = function(sudo) {
        this.sudo = sudo;
    };
    prototype.createProfile = function(orgId) {
        var profile = this.getProfile(orgId);
        if (profile == null) {
            this.addProfile(orgId, new live4api.UserProfile());
        }
    };
    prototype.setUserActive = function(orgId, active) {
        this.getProfile(orgId).active = active;
    };
    prototype.getOrgDepartment = function(orgId) {
        return this.getProfile(orgId).department;
    };
    prototype.setOrgDepartment = function(orgId, department) {
        this.getProfile(orgId).department = department;
    };
    prototype.getOrgTitle = function(orgId) {
        return this.getProfile(orgId).title;
    };
    prototype.setOrgTitle = function(orgId, title) {
        this.getProfile(orgId).title = title;
    };
    prototype.getOrgPhone = function(orgId) {
        return this.getProfile(orgId).phone;
    };
    prototype.setOrgPhone = function(orgId, phone) {
        this.getProfile(orgId).phone = phone;
    };
    prototype.getOrgNotes = function(orgId) {
        return this.getProfile(orgId).notes;
    };
    prototype.setOrgNotes = function(orgId, notes) {
        this.getProfile(orgId).notes = notes;
    };
    prototype.getProfile = function(orgId) {
        return this.profiles[orgId];
    };
    prototype.addProfile = function(orgId, userProfile) {
        this.profiles[orgId] = userProfile;
    };
}, {type: {name: "Enum", arguments: ["live4api.LoginType"]}, session: "live4api.AccessToken", profiles: {name: "Map", arguments: [null, "live4api.UserProfile"]}}, {});
stjs.ns("live4api");
live4api.Organization = function(name, orgAdminUserId) {
    this.name = name;
    this.ctime = stjs.trunc(new Date().getTime());
    this.active = true;
    this.userIds = [];
    this.orgAdminUserIds = [];
    if (orgAdminUserId != null) {
        this.userIds.push(orgAdminUserId);
        this.orgAdminUserIds.push(orgAdminUserId);
    }
    this.hardwareIds = [];
    this.address = new live4api.Address();
    this.billingInfo = new live4api.BillingInfo();
};
live4api.Organization = stjs.extend(live4api.Organization, null, [live4api.Doc], function(constructor, prototype) {
    prototype._rev = 0;
    prototype.id = null;
    prototype.name = null;
    prototype.description = null;
    prototype.active = false;
    prototype.ctime = 0;
    prototype.orgAdminUserIds = null;
    prototype.logoUrl = null;
    prototype.userIds = null;
    prototype.address = null;
    prototype.hardwareIds = null;
    prototype.missionIds = null;
    prototype.billingInfo = null;
    prototype.externalId = null;
    prototype._orgAdmins = null;
    prototype.getTheBestOrgAdminId = function() {
        return this.orgAdminUserIds[0];
    };
    prototype.addUserOrgAdmin = function(userId) {
        this.addUser(userId);
        this.addOrgAdmin(userId);
    };
    prototype.addOrgAdmin = function(userId) {
        if (this.orgAdminUserIds.indexOf(userId) == -1) {
            this.orgAdminUserIds.push(userId);
        }
    };
    prototype.addUser = function(userId) {
        if (this.userIds.indexOf(userId) == -1) {
            this.userIds.push(userId);
        }
    };
    prototype.removeUser = function(userId) {
        var idx = this.userIds.indexOf(userId);
        if (idx != -1) {
            this.userIds.splice(idx, 1);
        }
        this.removeOrgAdmin(userId);
    };
    prototype.removeOrgAdmin = function(userId) {
        var idx = this.orgAdminUserIds.indexOf(userId);
        if (idx != -1) {
            this.orgAdminUserIds.splice(idx, 1);
        }
    };
    prototype.containsUser = function(userId) {
        return this.userIds != null && this.userIds.indexOf(userId) != -1;
    };
    prototype.containsHardware = function(hwId) {
        return this.hardwareIds.indexOf(hwId) != -1;
    };
    prototype.addHardware = function(hardwareId) {
        if (this.hardwareIds.indexOf(hardwareId) == -1) {
            this.hardwareIds.push(hardwareId);
        }
    };
    prototype.removeHardware = function(hardwareId) {
        var idx = this.hardwareIds.indexOf(hardwareId);
        if (idx != -1) {
            this.hardwareIds.splice(idx, 1);
        }
    };
    prototype.listHardwareIds = function() {
        return this.hardwareIds;
    };
    prototype.getId = function() {
        return this.id;
    };
    prototype.setId = function(id) {
        this.id = id;
    };
    prototype.isActive = function() {
        return this.active;
    };
    prototype.getStatus = function() {
        if (this.active) {
            return "Active";
        } else {
            return "Inactive";
        }
    };
    prototype.hasOnlyOneAdmin = function() {
        return this.orgAdminUserIds.length == 1;
    };
}, {orgAdminUserIds: {name: "Array", arguments: [null]}, userIds: {name: "Array", arguments: [null]}, address: "live4api.Address", hardwareIds: {name: "Array", arguments: [null]}, missionIds: {name: "Array", arguments: [null]}, billingInfo: "live4api.BillingInfo", _orgAdmins: {name: "Array", arguments: ["live4api.User"]}}, {});
stjs.ns("live4api");
live4api.MissionPermissions = function() {};
live4api.MissionPermissions = stjs.extend(live4api.MissionPermissions, null, [], function(constructor, prototype) {
    constructor.canEditMisson = function(u, m) {
        return m.hasOwnerPermissions(u);
    };
    constructor.canEditStream = function(u, m) {
        return m.hasOwnerPermissions(u);
    };
    constructor.canAddUser = function(u, m) {
        return m.hasOwnerPermissions(u);
    };
    constructor.canEndMission = function(u, m) {
        return m.hasOwnerPermissions(u);
    };
    constructor.canEditLocations = function(u, m) {
        return m.hasOwnerPermissions(u);
    };
    constructor.canAssignPilot = function(u, m) {
        return m.hasPilotPermissions(u);
    };
    constructor.canUseChat = function(u, m) {
        return m.hasParticipantPermisisons(u);
    };
    constructor.canAddSources = function(u, m) {
        return m.hasParticipantPermisisons(u);
    };
    constructor.hasOneOwner = function(m) {
        return m.countOwners() == 1;
    };
    constructor.canViewCompletedMission = function(u, m) {
        return (live4api.Mission.State.ENDED == m.state) && (Internal.containsKey(m.roles, u.id) || live4api.Mission.isOrgAdmin(u, m));
    };
    constructor.canShareMission = function(user, mission) {
        if (mission == null || user == null) 
            return false;
        if (user.isSuperAdmin()) {
            return true;
        }
        if (null == user.getRole(mission.orgId) || live4api.UserRole.EXTERNAL == user.getRole(mission.orgId)) {
            return false;
        }
        var role = Internal.defaultMap(mission.roles)[user.id];
        if (live4api.MissionRole.UNKNOWN == role || null == role) {
            return false;
        }
        if (!user.belongsToOrg(mission.orgId)) {
            return false;
        }
        return true;
    };
    constructor.canRemoveUser = function(mission, me, you) {
        var meId = me.id;
        var youId = you.id;
        if (mission == null || meId == null || youId == null) 
            return false;
        var notRemovingMyself = !Internal.eq(youId, meId);
        var notRemovingOwnerOfMission = !live4api.Mission.isOwner(you, mission);
        return mission.hasOwnerPermissions(me) && notRemovingMyself && notRemovingOwnerOfMission;
    };
    constructor.canJoinMission = function(mission, userId) {
        return live4api.MissionPermissions.canPreviewMission(mission) && mission.hasUser(userId);
    };
    constructor.canPreviewMission = function(mission) {
        return mission != null && mission.state != live4api.Mission.State.CANCELLED;
    };
    constructor.canViewMission = function(mission, user) {
        var bool = live4api.MissionPermissions.canPreviewMission(mission);
        return (bool && mission.hasUser(user.id)) || (bool && live4api.Mission.isOrgAdmin(user, mission) && !mission.hasUser(user.id));
    };
    constructor.canStartMission = function(u, m) {
        return live4api.Mission.isScheduler(u, m) || live4api.Mission.isOrgAdmin(u, m) || live4api.Mission.isOwner(u, m);
    };
    constructor.userRemoved = function(oldMisison, newMission, me) {
        return oldMisison != null && oldMisison.roles[me.getId()] != null && newMission.roles[me.getId()] == null;
    };
}, {}, {});
stjs.ns("live4api");
live4api.StreamResponse = function() {
    live4api.Stream.call(this);
};
live4api.StreamResponse = stjs.extend(live4api.StreamResponse, live4api.Stream, [], function(constructor, prototype) {
    prototype.streamId = null;
    prototype.user = null;
    prototype.tags = null;
    prototype.durationMsec = 0;
    prototype.nearby = null;
    prototype.embedUrl = null;
    prototype.landUrl = null;
    prototype.city = null;
    prototype.likes = null;
    prototype.comments = null;
    prototype.hostUrl = null;
    prototype.flash = null;
    prototype.getHostUrl = function() {
        return this.hostUrl;
    };
    prototype.getFlash = function() {
        return this.flash;
    };
    prototype.userpic = function() {
        return this.user != null ? this.user.userpic : null;
    };
    prototype.username = function() {
        return this.user != null ? this.user.name : null;
    };
    prototype.isoDate = function() {
        return new Date(this.ctime).toISOString();
    };
}, {user: "live4api.UserResponse", tags: {name: "Array", arguments: ["live4api.Tag"]}, likes: "live4api.LikeResponse", comments: "live4api.CommentResponse", startGeoCoder: {name: "Array", arguments: ["GeocoderResult"]}, startLocation: "live4api.StreamLocation", status: {name: "Enum", arguments: ["live4api.LiveStatus"]}, privacy: {name: "Enum", arguments: ["live4api.Privacy"]}, tags2: {name: "Array", arguments: ["live4api.Tag"]}}, {});
//# sourceMappingURL=live4api3.map

module.exports = live4api;

