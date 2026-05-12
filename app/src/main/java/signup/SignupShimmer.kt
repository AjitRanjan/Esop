package signup

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@Composable
fun SignupShimmer() {
    Box(
        Modifier
            .fillMaxWidth()
            .height(80.dp)
            .background(Color.LightGray.copy(0.3f))
    )
}