package signup

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp


@Composable
fun InputField(
    value: String,
    onValueChange: (String) -> Unit,
    label: String,
    keyboardType: KeyboardType = KeyboardType.Text,   // ✅ ADD THIS
    modifier: Modifier = Modifier,
    isPassword: Boolean = false,
    isError: Boolean = false
) {
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        label = { Text(label) },
        modifier = modifier.fillMaxWidth(),
        visualTransformation = if (isPassword)
            PasswordVisualTransformation()
        else
            VisualTransformation.None,

        // ✅ ADD THIS
        keyboardOptions = KeyboardOptions(
            keyboardType = keyboardType
        ),

        isError = isError
    )
}

//@Composable
//fun InputField(
//    value: String,
//    onValueChange: (String) -> Unit,
//    label: String,
//    modifier: Modifier = Modifier,
//    keyboardType: KeyboardType = KeyboardType.Text,
//    isPassword: Boolean = false,
//    isError: Boolean = false
//) {
//    OutlinedTextField(
//        value = value,
//        onValueChange = onValueChange,
//        label = { Text(label) },
//        modifier = modifier.fillMaxWidth(),
//        keyboardOptions = KeyboardOptions(keyboardType = keyboardType),
//        visualTransformation = if (isPassword) PasswordVisualTransformation() else VisualTransformation.None,
//        isError = isError
//    )
//}
//@Composable
//fun InputField(
//    value: String,
//    onChange: (String) -> Unit,
//    label: String,
//    keyboardType: KeyboardType = KeyboardType.Text,
//    isPassword: Boolean = false
//) {
//    OutlinedTextField(
//        value = value,
//        onValueChange = onChange,
//        label = { Text(label) },
//        modifier = Modifier
//            .fillMaxWidth()
//            .padding(vertical = 4.dp),
//        keyboardOptions = KeyboardOptions(keyboardType = keyboardType),
//        visualTransformation = if (isPassword) PasswordVisualTransformation() else VisualTransformation.None
//    )
//}