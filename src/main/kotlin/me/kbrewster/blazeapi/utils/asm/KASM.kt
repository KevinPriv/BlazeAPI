/**
 * Kotlin ASM shortcuts
 */
@file:JvmName("KASM")
package me.kbrewster.blazeapi.utils.asm

import org.spongepowered.asm.lib.ClassReader
import org.spongepowered.asm.lib.MethodVisitor
import org.spongepowered.asm.lib.Opcodes.*
import org.spongepowered.asm.lib.Type
import org.spongepowered.asm.lib.tree.*


/**
 * Append a default return opcode to the given method insns list. The type will
 * be based off of the return value of the given method descriptor. Value is
 * always `0` for primitives and `null` for objects.
 *
 * @param insns
 * Method opcode list.
 * @param methodDesc
 * Method descriptor.
 */
fun appendReturn(insns: InsnList, methodDesc: String) {
    val retType = Type.getReturnType(methodDesc)
    when (retType.sort) {
        Type.BOOLEAN, Type.BYTE, Type.SHORT, Type.INT -> insns.add(InsnNode(ICONST_0))
        Type.LONG -> insns.add(InsnNode(LCONST_0))
        Type.FLOAT -> insns.add(InsnNode(FCONST_0))
        Type.DOUBLE -> insns.add(InsnNode(DCONST_0))
        Type.ARRAY, Type.OBJECT -> insns.add(InsnNode(ACONST_NULL))
    }
    insns.add(InsnNode(retType.getOpcode(IRETURN)))
}

/**
 * Fetch method in the given class by the given name and descriptor.
 *
 * @param cn
 * Class node containing the method.
 * @param name
 * Method name.
 * @param desc
 * Method descriptor.
 * @return Method matching given values. `null` if not found.
 */
fun getMethod(cn: ClassNode, name: String, desc: String): MethodNode? {
    for (mn in cn.methods) {
        if (mn.name == name && mn.desc == desc) {
            return mn
        }
    }
    return null
}

/**
 * Fetch field in the given class by the given name and descriptor.
 *
 * @param cn
 * Class node containing the field.
 * @param name
 * Field name.
 * @param desc
 * Field descriptor.
 * @return Field matching given values. `null` if not found.
 */
fun getField(cn: ClassNode, name: String, desc: String): FieldNode? {
    for (fn in cn.fields) {
        if (fn.name == name && fn.desc == desc) {
            return fn
        }
    }
    return null
}

/**
 * Creates a `ClassNode` from specified bytecode
 *
 * @param bytecode Class bytecode
 * @return ClassNode
 */
fun getClassNode(bytecode: ByteArray?): ClassNode? {
    if (bytecode == null)
        return null

    val cn = ClassNode()
    ClassReader(bytecode).accept(cn, 0)
    return cn
}

/**
 * Instruction builder for kotlin
 */
fun newInsnList(init: InsnList.() -> Unit): InsnList {
    val list = InsnList()
    list.init()
    return list
}

/**
 * Creates an array of integers of opcodes for loading variables at the given
 * local variable indices. For example if a method has the following local
 * variable table:
 *
 * <pre>
 * 1: int
 * 2: float
 * 3: Object
</pre> *
 *
 * Then the int array returned will be:
 *
 * <pre>
 * { ILOAD, FLOAD, ALOAD }
</pre> *
 *
 * @param mn
 * Method to parse for local types.
 * @return Array of opcodes matching local types.
 */
fun getLocalLoadInsns(mn: MethodNode): IntArray {
    val ret = IntArray(mn.maxLocals)
    val params = Type.getArgumentTypes(mn.desc)
    for (i in params.indices) {
        ret[i] = params[i].getOpcode(ILOAD)
    }
    mn.accept(object : MethodVisitor(ASM5) {
        override fun visitVarInsn(opcode: Int, `var`: Int) {
            when (opcode) {
                ILOAD, FLOAD, DLOAD, LLOAD, ALOAD -> ret[`var`] = opcode
                ISTORE -> ret[`var`] = ILOAD
                FSTORE -> ret[`var`] = FLOAD
                DSTORE -> ret[`var`] = DLOAD
                LSTORE -> ret[`var`] = LLOAD
                ASTORE -> ret[`var`] = ALOAD
            }
        }
    })
    return ret
}


