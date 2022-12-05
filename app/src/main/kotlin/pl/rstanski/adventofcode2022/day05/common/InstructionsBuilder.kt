package pl.rstanski.adventofcode2022.day05.common

object InstructionsBuilder {

    fun buildInstructions(drawing: Drawing): List<MoveInstruction> =
        drawing.instructions.map(::buildInstruction)

    private fun buildInstruction(instructionDrawing: String): MoveInstruction {
        val parts = instructionDrawing.split(" ")

        //sample instruction: move 1 from 2 to 1
        //instruction parts : 0    1 2    3 4  5

        require(parts.size == 6)
        return MoveInstruction(
            cratesCountToMove = parts[1].toInt(),
            fromStackNumber = parts[3].toInt(),
            toStackNumber = parts[5].toInt()
        )
    }
}