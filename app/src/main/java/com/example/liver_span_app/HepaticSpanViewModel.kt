import androidx.lifecycle.ViewModel

class HepaticSpanViewModel : ViewModel() {
    var height: Double = 0.0
    var age: Int = 0
    var liverSpan: Double = 0.0
    var classification: String = ""
    var classificationColor: String = ""

    fun calculateLiverSpan() {
        val offset = if (age >= 12) 5.04 else 3.00
        liverSpan = 12.6 * height - offset
        classifyHepatomegaly()
    }

    private fun classifyHepatomegaly() {
        classification = when {
            liverSpan < 10.0 -> {
                classificationColor = "Green"  // Normal
                "Normal"
            }
            liverSpan in 10.0..12.0 -> {
                classificationColor = "Yellow"  // Mild
                "Mild"
            }
            liverSpan in 12.1..14.0 -> {
                classificationColor = "Orange"  // Moderate
                "Moderate"
            }
            else -> {
                classificationColor = "Red"  // Severe
                "Severe"
            }
        }
    }

    fun validateInput(): Boolean {
        return height > 0 && age >= 0
    }
}