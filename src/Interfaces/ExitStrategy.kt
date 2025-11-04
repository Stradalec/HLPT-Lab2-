package interfaces
import kotlin.system.exitProcess

// нейросеть дала совет сделать такую прослойку чтобы тестирование стало реальным.
// функциональность программы при этом не меняется.

interface IExitStrategy {
    fun exit(code: Int): Nothing
}

// в CommandHandler.kt произошел replace exitProcess -> exitStrategy.exit
object RealExitStrategy : IExitStrategy {
    override fun exit(code: Int): Nothing = exitProcess(code)
}