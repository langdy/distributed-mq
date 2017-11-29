package com.yjl.fastjson.util;

import java.security.PrivilegedAction;
import java.util.HashMap;
import java.util.Map;
import com.yjl.fastjson.JSON;
import com.yjl.fastjson.JSONArray;
import com.yjl.fastjson.JSONAware;
import com.yjl.fastjson.JSONException;
import com.yjl.fastjson.JSONObject;
import com.yjl.fastjson.JSONPath;
import com.yjl.fastjson.JSONPathException;
import com.yjl.fastjson.JSONReader;
import com.yjl.fastjson.JSONStreamAware;
import com.yjl.fastjson.JSONWriter;
import com.yjl.fastjson.TypeReference;
import com.yjl.fastjson.parser.DefaultJSONParser;
import com.yjl.fastjson.parser.Feature;
import com.yjl.fastjson.parser.JSONLexer;
import com.yjl.fastjson.parser.JSONLexerBase;
import com.yjl.fastjson.parser.JSONReaderScanner;
import com.yjl.fastjson.parser.JSONScanner;
import com.yjl.fastjson.parser.JSONToken;
import com.yjl.fastjson.parser.ParseContext;
import com.yjl.fastjson.parser.ParserConfig;
import com.yjl.fastjson.parser.SymbolTable;
import com.yjl.fastjson.parser.deserializer.AutowiredObjectDeserializer;
import com.yjl.fastjson.parser.deserializer.DefaultFieldDeserializer;
import com.yjl.fastjson.parser.deserializer.ExtraProcessable;
import com.yjl.fastjson.parser.deserializer.ExtraProcessor;
import com.yjl.fastjson.parser.deserializer.ExtraTypeProvider;
import com.yjl.fastjson.parser.deserializer.FieldDeserializer;
import com.yjl.fastjson.parser.deserializer.JavaBeanDeserializer;
import com.yjl.fastjson.parser.deserializer.ObjectDeserializer;
import com.yjl.fastjson.serializer.AfterFilter;
import com.yjl.fastjson.serializer.BeanContext;
import com.yjl.fastjson.serializer.BeforeFilter;
import com.yjl.fastjson.serializer.ContextObjectSerializer;
import com.yjl.fastjson.serializer.ContextValueFilter;
import com.yjl.fastjson.serializer.JSONSerializer;
import com.yjl.fastjson.serializer.JavaBeanSerializer;
import com.yjl.fastjson.serializer.LabelFilter;
import com.yjl.fastjson.serializer.Labels;
import com.yjl.fastjson.serializer.NameFilter;
import com.yjl.fastjson.serializer.ObjectSerializer;
import com.yjl.fastjson.serializer.PropertyFilter;
import com.yjl.fastjson.serializer.PropertyPreFilter;
import com.yjl.fastjson.serializer.SerialContext;
import com.yjl.fastjson.serializer.SerializeBeanInfo;
import com.yjl.fastjson.serializer.SerializeConfig;
import com.yjl.fastjson.serializer.SerializeFilter;
import com.yjl.fastjson.serializer.SerializeFilterable;
import com.yjl.fastjson.serializer.SerializeWriter;
import com.yjl.fastjson.serializer.SerializerFeature;
import com.yjl.fastjson.serializer.ValueFilter;

public class ASMClassLoader extends ClassLoader {

    private static java.security.ProtectionDomain DOMAIN;

    private static Map<String, Class<?>> classMapping = new HashMap<String, Class<?>>();

    static {
        DOMAIN = (java.security.ProtectionDomain) java.security.AccessController
                .doPrivileged(new PrivilegedAction<Object>() {

                    public Object run() {
                        return ASMClassLoader.class.getProtectionDomain();
                    }
                });

        Class<?>[] jsonClasses = new Class<?>[] {JSON.class, JSONObject.class, JSONArray.class,
                JSONPath.class, JSONAware.class, JSONException.class, JSONPathException.class,
                JSONReader.class, JSONStreamAware.class, JSONWriter.class, TypeReference.class,

                FieldInfo.class, TypeUtils.class, IOUtils.class, IdentityHashMap.class,
                ParameterizedTypeImpl.class, JavaBeanInfo.class,

                ObjectSerializer.class, JavaBeanSerializer.class, SerializeFilterable.class,
                SerializeBeanInfo.class, JSONSerializer.class, SerializeWriter.class,
                SerializeFilter.class, Labels.class, LabelFilter.class, ContextValueFilter.class,
                AfterFilter.class, BeforeFilter.class, NameFilter.class, PropertyFilter.class,
                PropertyPreFilter.class, ValueFilter.class, SerializerFeature.class,
                ContextObjectSerializer.class, SerialContext.class, SerializeConfig.class,

                JavaBeanDeserializer.class, ParserConfig.class, DefaultJSONParser.class,
                JSONLexer.class, JSONLexerBase.class, ParseContext.class, JSONToken.class,
                SymbolTable.class, Feature.class, JSONScanner.class, JSONReaderScanner.class,

                AutowiredObjectDeserializer.class, ObjectDeserializer.class, ExtraProcessor.class,
                ExtraProcessable.class, ExtraTypeProvider.class, BeanContext.class,
                FieldDeserializer.class, DefaultFieldDeserializer.class,};

        for (Class<?> clazz : jsonClasses) {
            classMapping.put(clazz.getName(), clazz);
        }
    }

    public ASMClassLoader() {
        super(getParentClassLoader());
    }

    public ASMClassLoader(ClassLoader parent) {
        super(parent);
    }

    static ClassLoader getParentClassLoader() {
        ClassLoader contextClassLoader = Thread.currentThread().getContextClassLoader();
        if (contextClassLoader != null) {
            try {
                contextClassLoader.loadClass(JSON.class.getName());
                return contextClassLoader;
            } catch (ClassNotFoundException e) {
                // skip
            }
        }
        return JSON.class.getClassLoader();
    }

    protected Class<?> loadClass(String name, boolean resolve) throws ClassNotFoundException {
        Class<?> mappingClass = classMapping.get(name);
        if (mappingClass != null) {
            return mappingClass;
        }

        try {
            return super.loadClass(name, resolve);
        } catch (ClassNotFoundException e) {
            throw e;
        }
    }

    public Class<?> defineClassPublic(String name, byte[] b, int off, int len)
            throws ClassFormatError {
        Class<?> clazz = defineClass(name, b, off, len, DOMAIN);

        return clazz;
    }

    public boolean isExternalClass(Class<?> clazz) {
        ClassLoader classLoader = clazz.getClassLoader();

        if (classLoader == null) {
            return false;
        }

        ClassLoader current = this;
        while (current != null) {
            if (current == classLoader) {
                return false;
            }

            current = current.getParent();
        }

        return true;
    }

}
