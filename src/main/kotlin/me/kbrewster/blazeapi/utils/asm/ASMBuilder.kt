package me.kbrewster.blazeapi.utils.asm

import org.spongepowered.asm.lib.tree.AbstractInsnNode
import org.spongepowered.asm.lib.tree.ClassNode
import org.spongepowered.asm.lib.tree.InsnList
import org.spongepowered.asm.lib.tree.MethodNode

class JVMPackage(val pkg: String) {
    fun visitClass(clazz: String, init: JVMClass.() -> Unit): JVMClass {
        val jvmClass = JVMClass("$pkg.$clazz")
        jvmClass.init()
        return jvmClass
    }
}

class JVMClass(val clazz: String) {
    fun visitMethod(methodSignature: String, init: JVMMethod.() -> Unit): JVMMethod {
        val jvmMethod = JVMMethod(clazz, methodSignature)
        jvmMethod.init()
        return jvmMethod
    }
}

class JVMMethod(val clazzName: String, val signature: String) {

    constructor(clazz: Class<*>, signature: String) : this(clazz.name, signature)


    fun visitInstructions(methodNode: MethodNode, init: JVMInstruction.() -> Unit): JVMInstruction {
        val jvmInstruction = JVMInstruction(methodNode.instructions)
        jvmInstruction.init()
        return jvmInstruction
    }


    fun getMethodNode(classNode: ClassNode): MethodNode {
        return classNode.methods.first { it.name == signature }
    }
}

class JVMInstruction(val insnList: InsnList) {

    operator fun plusAssign(node: AbstractInsnNode) {
        insnList.add(node)
    }

    operator fun plusAssign(node: InsnList) {
        insnList.add(node)
    }

}

fun jvmPackage(pkg: String, init: JVMPackage.() -> Unit): JVMPackage {
    val jvmPkg = JVMPackage(pkg)
    jvmPkg.init()
    return jvmPkg
}