package pl.rstanski.adventofcode2022.day16.common

object PipeParser {

    fun parse(line: String): Pipe {
        //Valve AA has flow rate=0; tunnels lead to valves DD, II, BB
        val valve = line.drop("Valve ".length).take(2)
        val rateAndLead = line.drop("Valve AA has flow rate=".length).split(";")
        val rate = rateAndLead[0].toInt()
        val lead = if (rateAndLead[1].contains("tunnels")) {
            rateAndLead[1].drop(" tunnels lead to valves ".length).replace(" ", "").split(",")
        } else {
            rateAndLead[1].drop(" tunnel lead to valves ".length).replace(" ", "").split(",")
        }

        return Pipe(valve, rate, lead)
    }
}

data class Pipe(val valve: String, val rate: Int, val leadsTo: List<String>)