package me.kbrewster.blazeapi.internal.addons

import com.google.common.base.Stopwatch
import me.kbrewster.blazeapi.LOGGER
import me.kbrewster.blazeapi.internal.addons.misc.AddonLoadException
import me.kbrewster.blazeapi.internal.addons.strategy.AddonLoaderStrategy
import me.kbrewster.blazeapi.internal.addons.strategy.DefaultAddonLoader
import me.kbrewster.blazeapi.internal.addons.strategy.WorkspaceAddonLoader
import net.minecraft.launchwrapper.Launch
import java.io.File
import java.util.*

/**
 * Instance created on the classloader sun.misc.Launcher$AppClassLoader
 *
 * @since 1.0
 * @author Kevin Brewster
 */
object AddonBootstrap {

    /**
     * Directory where all the addonManifests are stored
     */
    private val modDirectory = File("addon")

    /**
     * Current (active) environment phase, set to NULL until the
     * phases have been populated
     */
    var phase: Phase? = null

    /**
     * All the filtered jars inside of the {@link #modDirectory} folder,
     */
    private val jars: ArrayList<File>

    /**
     * Method of loading all the valid addonManifests to the classloader
     */
    private val loader = DefaultAddonLoader()

    /**
     * Method of loading the addon if inside of the development environment
     */
    private val workspaceLoader = WorkspaceAddonLoader()

    /**
     * Once the {@link #init} has called it will then be populated
     */
    val addonManifests = ArrayList<AddonManifest>()

    /**
     * Loads of the files inside {@link #modDirectory} folder or
     * creates the directory if not already made and then adds
     * all the valid jar files to {@link #jars}
     */
    init {
        if (!modDirectory.mkdirs() && !modDirectory.exists()) {
            throw AddonLoadException("Unable to create addon directory!")
        }

        jars = modDirectory.listFiles()!!
                .filter { it.name.toLowerCase().endsWith(".jar") }
                .toCollection(ArrayList())

    }

    /**
     * The Preinit phase of the bootstrap where all the
     * Addon Manifests are loaded into {@link #addonManifests}
     * and then sets the phase to {@link Phase#INIT}.
     *
     * This should be called before Minecraft's classes are loaded.
     * In this case we use {@link net.minecraft.launchwrapper.ITweaker}
     */
    fun init() {
        if (phase != null) {
            throw AddonLoadException("Cannot initialise bootstrap twice")
        }

        phase = Phase.PREINIT
        Launch.classLoader.addClassLoaderExclusion("me.kbrewster.blazeapi.internal.addons.AddonBootstrap")
        Launch.classLoader.addClassLoaderExclusion("me.kbrewster.blazeapi.internal.addons.AddonManifest")

        with(addonManifests) {
            val workspaceAddon = loadWorkspaceAddon()
            //TODO: ADD DEV ENVIRONMENT CHECK
            if (workspaceAddon != null) {
                add(workspaceAddon)
            }
            addAll(loadAddons(loader))
        }

        phase = Phase.INIT
    }

    /**
     * This loads the internal addon meaning that this must be inside of a development
     * environment.
     *
     * @return returns the addon manifest
     */
    private fun loadWorkspaceAddon(): AddonManifest? {
        return try {
            loadAddon(workspaceLoader, null)
        } catch (ignored: Exception) {
            ignored.printStackTrace()
            null
        }
    }

    /**
     * Loads external jars using {@link me.kbrewster.blazeapi.internal.addons.strategy.AddonLoaderStrategy}
     * This extracts the <i>addon.json</i> from the addon and returns the parsed manifest
     *
     * @param loader Addon loader
     * @return returns a list of the addon manifests
     */
    private fun loadAddons(loader: AddonLoaderStrategy): List<AddonManifest> {
        val addons = ArrayList<AddonManifest>()
        val benchmark = Stopwatch.createStarted()
        LOGGER.info("Starting to load external jars...")
        for (jar in jars) {
            try {
                val addon = loadAddon(loader, jar) ?: continue
                addons.add(addon)
            } catch (e: Exception) {
                LOGGER.error("Could not load {}!", jar.name)
                e.printStackTrace()
            }
        }
        LOGGER.debug("Finished loading all jars in {}.", benchmark)
        return addons
    }


    /**
     * Loads a jar using {@link me.kbrewster.blazeapi.internal.addons.strategy.AddonLoaderStrategy}
     * This extracts the <i>addon.json</i> from the addon and returns the parsed manifest
     *
     * @param loader Addon loader strategy
     * @param addon The file which is to be loaded, can be left null if [loader] does not need it
     * @return returns the addon manifest
     */
    @Throws(Exception::class)
    private fun loadAddon(loader: AddonLoaderStrategy, addon: File?): AddonManifest? {
        return loader.load(addon)
    }


    /**
     * Phase the bootstrap is currently in
     */
    enum class Phase {

        /**
         * Loading classes into classloader
         * Executing AddonManifest {@link net.minecraft.launchwrapper.ITweaker} classes
         */
        PREINIT,

        /**
         * Creates instances of main classes and executes onLoad
         */
        INIT,

        /**
         * If this all phases goes successfully it will end up on the defaulted phase
         */
        DEFAULT;
    }
}
