package me.kbrewster.blazeapi.internal.launch

import me.kbrewster.blazeapi.internal.addons.AddonBootstrap
import me.kbrewster.blazeapi.utils.net.URLReader
import net.minecraft.launchwrapper.ITweaker
import net.minecraft.launchwrapper.LaunchClassLoader
import org.spongepowered.asm.launch.MixinBootstrap
import org.spongepowered.asm.mixin.MixinEnvironment
import org.spongepowered.asm.mixin.Mixins
import java.io.File


open class BlazeTweaker : ITweaker {

    private val args: ArrayList<String> = ArrayList()

    override fun acceptOptions(args: MutableList<String>, gameDir: File?, assetsDir: File?, profile: String?) {
        this.args.addAll(args)
        addArgs(mapOf("gameDir" to gameDir,
                "assetsDir" to assetsDir,
                "version" to profile))
    }

    override fun getLaunchTarget() =
            "net.minecraft.client.main.Main"

    override fun injectIntoClassLoader(cl: LaunchClassLoader) {
        // excludes from classloader
        cl.addClassLoaderExclusion("org.apache.logging.log4j.")
        cl.addClassLoaderExclusion("kotlin.")

        // initialises bootstraps
        MixinBootstrap.init()
        AddonBootstrap.init()

        // sets up mixin configurations
        with(MixinEnvironment.getDefaultEnvironment()) {
            Mixins.addConfiguration("mixins.blazeapi.json")
            this.obfuscationContext = "notch"
            this.side = MixinEnvironment.Side.CLIENT
        }

        val url = URLReader("https://api.hyperium.cc/capeAtlas")
        println(url.responseType)
        print(url.json)
    }


    override fun getLaunchArguments(): Array<String> = args.toTypedArray()

    private fun addArg(label: String, value: String?) {
        if (!this.args.contains("--$label") && value != null) {
            this.args.add("--$label")
            this.args.add(value)
        }
    }

    private fun addArg(args: String, file: File?) {
        file?.let {
            addArg(args, file.absolutePath)
        }
    }

    private fun addArgs(args: Map<String, Any?>) {
        args.forEach{ label, value ->
            when (value) {
                is String? -> addArg(label, value)
                is File? -> addArg(label, value)
            }
        }
    }

}