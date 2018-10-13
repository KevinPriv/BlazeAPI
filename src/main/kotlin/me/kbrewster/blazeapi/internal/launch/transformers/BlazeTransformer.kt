package me.kbrewster.blazeapi.internal.launch.transformers

import net.minecraft.launchwrapper.IClassTransformer

class BlazeTransformer : IClassTransformer {

    override fun transform(name: String?, transformedName: String?, basicClass: ByteArray?): ByteArray {
        /* val node = getClassNode(basicClass) ?: return basicClass!!
        val classTransformers = TRANSFORMERS.filter { it.targets.any { it == name } || it.targets.isEmpty() }
        if (classTransformers.isNotEmpty()) {
            classTransformers.forEach { it.transform(node) }
            val cw = ClassWriter(ClassWriter.COMPUTE_MAXS or ClassWriter.COMPUTE_FRAMES)
            node.accept(cw)
            return cw.toByteArray()
        }
        */
        return basicClass!!
    }

}