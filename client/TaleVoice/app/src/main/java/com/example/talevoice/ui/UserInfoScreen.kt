package com.example.talevoice.ui
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.example.talevoice.viewmodel.UserInfoViewModel


@Composable
fun UserInfoScreen(
    navController: NavHostController,
    userInfoViewModel: UserInfoViewModel = viewModel()) {

    val name = userInfoViewModel.name
    val gender = userInfoViewModel.gender

    val unselectedColor = MaterialTheme.colorScheme.primaryContainer
    val selectedColor = MaterialTheme.colorScheme.primary

    // 저장 버튼 활성화 조건: 이름과 성별이 모두 입력된 경우
    val isSaveEnabled = name.isNotEmpty() && gender.isNotEmpty()

    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Column(
            modifier = Modifier.fillMaxWidth(),
            verticalArrangement = Arrangement.Center
        ) {
            // 상단 타이틀
            Text(
                text = "TaleVoice",
                fontFamily = FontFamily.Serif,
                fontWeight = FontWeight.SemiBold,
                style = MaterialTheme.typography.headlineLarge,
                modifier = Modifier.padding(bottom = 16.dp)
            )

            // 안내 문구
            Text(
                text = "이름을 입력해주세요",
                fontFamily = FontFamily.Serif,
                fontWeight = FontWeight.Thin,
                style = MaterialTheme.typography.headlineSmall,
                modifier = Modifier.padding(bottom = 24.dp)
            )

            // 입력 필드
            OutlinedTextField(
                value = name,
                onValueChange = { newName ->
                    userInfoViewModel.onNameChanged(newName) // Delegate to ViewModel
                },
                label = { Text("Input") },
                trailingIcon = {
                    if (name.isNotEmpty()) {
                        IconButton(onClick = { userInfoViewModel.onNameChanged("") }) {
                            Icon(
                                imageVector = Icons.Default.Close,
                                contentDescription = "Clear text"
                            )
                        }
                    }
                },
                supportingText = {
                    Text("Supporting text")
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 24.dp)
            )

            // Segmented Button for Gender Selection
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 24.dp),
                horizontalArrangement = Arrangement.Center
            ) {
                listOf("남성", "여성").forEach { genderOption ->
                    Button(
                        onClick = { userInfoViewModel.onGenderChanged(genderOption) },
                        shape = RoundedCornerShape(0.dp),
                        colors = androidx.compose.material3.ButtonDefaults.buttonColors(
                            containerColor = if (gender == genderOption) selectedColor else unselectedColor,
                            contentColor = if (gender == genderOption) MaterialTheme.colorScheme.onPrimary else MaterialTheme.colorScheme.onSurface
                        ),
                        modifier = Modifier
                            .weight(1f)
                            .padding(horizontal = 4.dp)
                    ) {
                        Text(text = genderOption)
                    }
                }
            }

            // 저장 버튼
            Button(
                onClick = {
                    navController.navigate("TaleList/${userInfoViewModel.name}/${userInfoViewModel.gender}")
                },
                enabled = isSaveEnabled, // 이름과 성별이 모두 입력된 경우 활성화
                colors = androidx.compose.material3.ButtonDefaults.buttonColors(
                    containerColor = if (isSaveEnabled) selectedColor else unselectedColor,
                    contentColor = if (isSaveEnabled) MaterialTheme.colorScheme.onPrimary else MaterialTheme.colorScheme.onSurface
                ),
                shape = RoundedCornerShape(0.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("저장")
            }
        }
    }
}

