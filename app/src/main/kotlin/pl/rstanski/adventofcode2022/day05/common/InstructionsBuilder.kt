package pl.rstanski.adventofcode2022.day05.common

object InstructionsBuilder {

    fun buildInstructions(drawing: Drawing): List<Instruction> {
        return drawing.instructions.map { instructionDrawing ->
            val parts = instructionDrawing.split(" ")
            Instruction(parts[1].toInt(), parts[3].toInt(), parts[5].toInt())
        }
    }
}