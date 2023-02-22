package invtweaks.forge.asm;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.Type;
import org.objectweb.asm.tree.*;

public class ASMHelper {
    /**
     * Generate a new method "boolean name()", returning a constant value
     *
     * @param clazz  Class to add method to
     * @param name   Name of method
     * @param retval Return value of method
     */
    public static void generateBooleanMethodConst(@NotNull ClassNode clazz, String name, boolean retval) {
        @Nullable MethodNode method = new MethodNode(Opcodes.ASM4, Opcodes.ACC_PUBLIC | Opcodes.ACC_SYNTHETIC, name, "()Z", null, null);
        InsnList code = method.instructions;

        code.add(new InsnNode(retval ? Opcodes.ICONST_1 : Opcodes.ICONST_0));
        code.add(new InsnNode(Opcodes.IRETURN));

        clazz.methods.add(method);
    }

    /**
     * Generate a new method "int name()", returning a constant value
     *
     * @param clazz  Class to add method to
     * @param name   Name of method
     * @param retval Return value of method
     */
    public static void generateIntegerMethodConst(@NotNull ClassNode clazz, String name, short retval) {
        @NotNull MethodNode method = new MethodNode(Opcodes.ASM4, Opcodes.ACC_PUBLIC | Opcodes.ACC_SYNTHETIC, name, "()I", null, null);
        InsnList code = method.instructions;

        // Probably doesn't make a huge difference, but use BIPUSH if the value is small enough.
        if(retval >= Byte.MIN_VALUE && retval <= Byte.MAX_VALUE) {
            code.add(new IntInsnNode(Opcodes.BIPUSH, retval));
        } else {
            code.add(new IntInsnNode(Opcodes.SIPUSH, retval));
        }
        code.add(new InsnNode(Opcodes.IRETURN));

        clazz.methods.add(method);
    }

    /**
     * Generate a forwarding method of the form "T name() { return this.forward(); }
     *
     * @param clazz       Class to generate new method on
     * @param name        Name of method to generate
     * @param forwardname Name of method to call
     * @param rettype     Return type of method
     */
    public static void generateSelfForwardingMethod(@NotNull ClassNode clazz, String name, String forwardname, @NotNull Type rettype) {
        @Nullable MethodNode method = new MethodNode(Opcodes.ASM4, Opcodes.ACC_PUBLIC | Opcodes.ACC_SYNTHETIC, name, "()" + rettype.getDescriptor(), null, null);

        populateSelfForwardingMethod(method, forwardname, rettype, Type.getObjectType(clazz.name));

        clazz.methods.add(method);
    }

    /**
     * Generate a forwarding method of the form "T name() { return Class.forward(this); }
     *
     * @param clazz       Class to generate new method on
     * @param name        Name of method to generate
     * @param forwardname Name of method to call
     * @param rettype     Return type of method
     */
    public static void generateForwardingToStaticMethod(@NotNull ClassNode clazz, String name, String forwardname, @NotNull Type rettype, @NotNull Type fowardtype) {
        @NotNull MethodNode method = new MethodNode(Opcodes.ASM4, Opcodes.ACC_PUBLIC | Opcodes.ACC_SYNTHETIC, name, "()" + rettype.getDescriptor(), null, null);

        populateForwardingToStaticMethod(method, forwardname, rettype, Type.getObjectType(clazz.name), fowardtype);

        clazz.methods.add(method);
    }

    /**
     * Generate a forwarding method of the form "T name() { return Class.forward(this); }
     *
     * @param clazz       Class to generate new method on
     * @param name        Name of method to generate
     * @param forwardname Name of method to call
     * @param rettype     Return type of method
     * @param thistype    Type to treat 'this' as for overload searching purposes
     */
    public static void generateForwardingToStaticMethod(@NotNull ClassNode clazz, String name, String forwardname, @NotNull Type rettype, @NotNull Type fowardtype, @NotNull Type thistype) {
        @NotNull MethodNode method = new MethodNode(Opcodes.ASM4, Opcodes.ACC_PUBLIC | Opcodes.ACC_SYNTHETIC, name, "()" + rettype.getDescriptor(), null, null);

        populateForwardingToStaticMethod(method, forwardname, rettype, thistype, fowardtype);

        clazz.methods.add(method);
    }

    /**
     * Replace a method's code with a forward to another method on itself (or the first argument of a static method as
     * the argument takes the place of this)
     *
     * @param method      Method to replace code of
     * @param forwardname Name of method to forward to
     * @param thistype    Type of object method is being replaced on
     */
    public static void replaceSelfForwardingMethod(@NotNull MethodNode method, String forwardname, @NotNull Type thistype) {
        Type methodType = Type.getMethodType(method.desc);

        method.instructions.clear();

        populateSelfForwardingMethod(method, forwardname, methodType.getReturnType(), thistype);
    }

    /**
     * Populate a forwarding method of the form "T name() { return Class.forward(this); }"
     *
     * @param method      Method to generate code for
     * @param forwardname Name of method to call
     * @param rettype     Return type of method
     * @param thistype    Type of object method is being generated on
     * @param forwardtype Type to forward method to
     */
    public static void populateForwardingToStaticMethod(@NotNull MethodNode method, String forwardname, @NotNull Type rettype, @NotNull Type thistype, @NotNull Type forwardtype) {
        InsnList code = method.instructions;

        code.add(new VarInsnNode(thistype.getOpcode(Opcodes.ILOAD), 0));
        code.add(new MethodInsnNode(Opcodes.INVOKESTATIC, forwardtype.getInternalName(), forwardname, Type.getMethodDescriptor(rettype, thistype), false));
        code.add(new InsnNode(rettype.getOpcode(Opcodes.IRETURN)));
    }

    /**
     * Populate a forwarding method of the form "T name() { return this.forward(); }" This is also valid for methods of
     * the form "static T name(S object) { return object.forward() }"
     *
     * @param method      Method to generate code for
     * @param forwardname Name of method to call
     * @param rettype     Return type of method
     * @param thistype    Type of object method is being generated on
     */
    public static void populateSelfForwardingMethod(@NotNull MethodNode method, String forwardname, @NotNull Type rettype, @NotNull Type thistype) {
        InsnList code = method.instructions;

        code.add(new VarInsnNode(thistype.getOpcode(Opcodes.ILOAD), 0));
        code.add(new MethodInsnNode(Opcodes.INVOKEVIRTUAL, thistype.getInternalName(), forwardname, "()" + rettype.getDescriptor(), false));
        code.add(new InsnNode(rettype.getOpcode(Opcodes.IRETURN)));
    }
}