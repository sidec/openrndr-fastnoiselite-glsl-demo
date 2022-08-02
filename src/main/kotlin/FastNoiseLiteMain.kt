import org.openrndr.application
import org.openrndr.draw.Filter
import org.openrndr.draw.filterWatcherFromUrl
import org.openrndr.draw.renderTarget
import org.openrndr.extra.gui.GUI
import org.openrndr.extra.parameters.DoubleParameter
import org.openrndr.extra.parameters.IntParameter
import org.openrndr.extra.parameters.OptionParameter
import org.openrndr.math.IntVector3
import org.openrndr.math.Vector2
import org.openrndr.math.Vector3
import java.nio.file.Paths


enum class NoiseType(val type: Int) {
    OPENSIMPLEX2(0),
    OPENSIMPLEX2S(1),
    CELLULAR(2),
    PERLIN(3),
    VALUE_CUBIC(4),
    VALUE(5)
}
enum class Rotation3dType(val type: Int) {
    NONE(0),
    IMPROVE_XY_PLANES(1),
    IMPROVE_XZ_PLANES(2),

}
enum class FractalType(val type: Int) {
    NONE(0),
    FBM(1),
    RIDGED(2),
    PINGPONG(3),
    PROGRESSIVE(4),
    INDEPENDENT(5)
}
enum class DomainWarpType(val type: Int) {
    OPENSIMPLEX2(0),
    OPENSIMPLEX2_REDUCED(1),
    BASIC_GRID(2)
}
enum class DomainWarpFractal(val type: Int) {
    NONE(0),
    PROGRESSIVE(4),
    INDEPENDENT(5)
}
enum class CellularReturnValue(val type: Int) {
    CELLVALUE(0),
    DISTANCE(1),
    DISTANCE2(2),
    DISTANCE2ADD(3),
    DISTANCE2SUB(4),
    DISTANCE2MUL(5),
    DISTANCE2DIV(6)
}
enum class CellularDistanceFunc(val type: Int) {
    EUCLIDEAN(0),
    EUCLIDEANSQ(1),
    MANHATTAN(2),
    HYBRID(3)
}

fun main() = application {
    configure {
        width = 968
        height = 768
    }

    program {
        // Shader
        val fragPath = "file:${Paths.get("","src/main/resources/fast_noise_lite.frag" ) }"
        val watcher = filterWatcherFromUrl(fragPath)
        val fastNoise = object : Filter(null, watcher) {
            var uResolution: Vector3 by parameters
            var uTime: Double by parameters
            var uFrequency: Double by parameters
            var uLacunarity: Double by parameters
            var uGain: Double by parameters
            var uWeightedStrength: Double by parameters
            var uPingPongStrength: Double by parameters
            var uCellularJitterMod: Double by parameters
            var uOctaves: Int by parameters
            var uDomainWarpAmp: Double by parameters
            var uDomainWarpType: Int by parameters
            var uDomainWarpFractal: Int by parameters
            var uOffset: Vector2 by parameters
            var uFractalType: Int by parameters
            var uNoiseType: Int by parameters
            var uRotation3dType: Int by parameters
            var uCellularReturnValue: Int by parameters
            var uCellularDistanceFunc: Int by parameters
        }


        val gui = GUI()
//        gui.persistState = false
        val settings = object {
            @OptionParameter("noise type", order=0)
            var noise_type = NoiseType.OPENSIMPLEX2
                set(value) {
                    fastNoise.uNoiseType = value.type
                    field = value
                }

            @OptionParameter("rotation 3d type", order=1)
            var rotation3d_type = Rotation3dType.NONE
                set(value) {
                    fastNoise.uRotation3dType = value.type
                    field = value
                }

            @DoubleParameter("frequency", 0.0, 10.0, order=2)
            var frequency = 10.0
                set(value) {
                    fastNoise.uFrequency = value
                    field = value

                }

            @OptionParameter("fractal type", order=3)
            var fractal_type = FractalType.NONE
                set(value) {
                    fastNoise.uFractalType = value.type
                    field = value
                }
            @IntParameter("octaves", 2, 8, order=4)
            var octaves = 4
                set(value) {
                    fastNoise.uOctaves = value
                    field = value
                }

            @DoubleParameter("lacunarity", 0.1, 4.0, order=5)
            var lacunarity = 2.0
                set(value) {
                    fastNoise.uLacunarity = value
                    field = value

                }
            @DoubleParameter("gain", -2.1, 4.1, order=6)
            var gain = 0.5
                set(value) {
                    fastNoise.uGain = value
                    field = value
                }
            @DoubleParameter("weighted_strength", 0.05, 8.0, order=7)
            var weighted_strength = 2.0
                set(value) {
                    fastNoise.uWeightedStrength = value
                    field = value
                }

            @DoubleParameter("ping pong strength", -20.005, 20.0, order=8)
            var ping_pong_strength = 10.0
                set(value) {
                    fastNoise.uPingPongStrength = value
                    field = value
                }

            @DoubleParameter("cellular jitter mod", -1.005, 4.005)
            var cellular_jitter_mod = 0.95
                set(value) {
                    fastNoise.uCellularJitterMod = value
                    field = value
                }

            @OptionParameter("warp type")
            var domain_warp_type = DomainWarpType.OPENSIMPLEX2
                set(value) {
                    fastNoise.uDomainWarpType = value.type
                    field = value
                }
            @OptionParameter("warp fractal")
            var domain_warp_fractal = DomainWarpFractal.NONE
                set(value) {
                    fastNoise.uDomainWarpFractal = value.type
                    field = value
                }

            @OptionParameter("cellular value")
            var cellular_return_value = CellularReturnValue.CELLVALUE
                set(value) {
                    fastNoise.uCellularReturnValue = value.type
                    field = value
                }
            @OptionParameter("cellular func")
            var cellular_distance_func = CellularDistanceFunc.EUCLIDEAN
                set(value) {
                    fastNoise.uCellularDistanceFunc = value.type
                    field = value
                }

            @DoubleParameter("domain warp amp", 0.005, 100.005)
            var domain_warp_amp = 30.0
                set(value) {
                    fastNoise.uDomainWarpAmp = value
                    field = value
                }
        }
        gui.add(settings, "FastNoiseLite Parameters")

        with (fastNoise) {
            uResolution = IntVector3(width,height, 0).vector3
            with (settings) {
                uFrequency = frequency
                uLacunarity = lacunarity
                uOctaves = octaves
                uWeightedStrength = weighted_strength
                uPingPongStrength = ping_pong_strength
                uCellularJitterMod = cellular_jitter_mod
                uGain = gain
                uDomainWarpAmp = domain_warp_amp
                uOffset = Vector2.ZERO
            }
        }

        val offscreen = renderTarget(width, height) {
            colorBuffer()
            depthBuffer()
        }

        gui.enableSideCanvas=true
        extend(gui)
        extend {

            fastNoise.uTime = seconds
            fastNoise.apply(offscreen.colorBuffer(0), offscreen.colorBuffer(0))
            drawer.image(offscreen.colorBuffer(0),200.0,0.0)

        }
    }
}
