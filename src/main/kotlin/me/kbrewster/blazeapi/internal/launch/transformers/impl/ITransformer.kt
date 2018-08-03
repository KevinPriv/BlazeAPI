package me.kbrewster.blazeapi.internal.launch.transformers.impl

import org.spongepowered.asm.lib.tree.ClassNode

interface ITransformer {

    val targets: Array<String>

    fun transform(node: ClassNode)


}