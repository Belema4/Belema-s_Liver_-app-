import androidx.lifecycle.ViewModel

class HepaticSpanViewModel : ViewModel() {
    // Function to calculate liver span based on Belema's Law
    fun calculateLiverSpan(height: Double, weight: Double, age: Int): Double {
        // Implement the calculation based on Belema's Law
        return (height + weight + age) / 3 // Example formula
    }
}