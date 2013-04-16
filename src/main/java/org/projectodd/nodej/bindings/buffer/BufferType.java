package org.projectodd.nodej.bindings.buffer;

import org.dynjs.exception.ThrowException;
import org.dynjs.runtime.AbstractNativeFunction;
import org.dynjs.runtime.DynArray;
import org.dynjs.runtime.DynObject;
import org.dynjs.runtime.ExecutionContext;
import org.dynjs.runtime.GlobalObject;
import org.dynjs.runtime.PropertyDescriptor;
import org.projectodd.nodej.bindings.buffer.prototype.ByteLength;

public class BufferType extends  AbstractNativeFunction { 
    public BufferType(GlobalObject globalObject) {
        super(globalObject);
        this.setClassName("Buffer");
        final DynObject prototype = initializePrototype(globalObject);
        defineOwnProperty(null, "prototype", new PropertyDescriptor() {
            {
                set("Value", prototype);
                set("Writable", false);
                set("Configurable", false);
                set("Enumerable", false);
            }
        }, false);
        setPrototype(prototype);
    }

    @Override
    public Object call(ExecutionContext context, Object self, Object... args) {
        Buffer buffer;
        if (args[0] instanceof DynArray) {
            buffer = new Buffer(context.getGlobalObject(), (DynArray)args[0]);
        } else if (args[0] instanceof Number){
            buffer = new Buffer(context.getGlobalObject(), (long) args[0]);
        } else if (args[0] instanceof String) {
            String str = (String) args[0];
            Buffer.Encoding encoding = Buffer.Encoding.UTF8;
            if (args.length > 1 && args[1] instanceof String) {
                encoding = Buffer.getEncoding((String)args[1]);
            }
            buffer = new Buffer(context.getGlobalObject(), str, str.length(), encoding);
        } else {
            throw new ThrowException(context, context.createTypeError("Bad argument"));
        }
        buffer.setPrototype(this.getPrototype());
        return buffer;
    }
    
    private DynObject initializePrototype(GlobalObject globalObject) {
        final DynObject prototype = new DynObject(globalObject);
        prototype.defineReadOnlyProperty(globalObject, "byteLength", new ByteLength(globalObject));
        prototype.defineOwnProperty(null, "constructor", new PropertyDescriptor() {
            {
                set("Value", this);
                set("Writable", false);
                set("Configurable", false);
                set("Enumerable", true);
            }
        }, false);
        return prototype;
    }
    
}
