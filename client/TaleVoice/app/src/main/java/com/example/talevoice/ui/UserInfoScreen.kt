package com.example.talevoice.ui
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController


@Composable
fun UserInfoScreen(navController: NavHostController) {
    var userName by remember { mutableStateOf("") }
    var selectedGender by remember { mutableStateOf<String?>(null) }

    val unselectedColor = MaterialTheme.colorScheme.primaryContainer
    val selectedColor = MaterialTheme.colorScheme.primary

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
                value = userName,
                onValueChange = { userName = it },
                label = { Text("Input") },
                trailingIcon = {
                    if (userName.isNotEmpty()) {
                        IconButton(onClick = { userName = "" }) {
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

            // 성별 선택 버튼 영역
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 24.dp),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                Button(
                    onClick = { selectedGender = "남성" },
                    shape = RoundedCornerShape(0.dp),
                    modifier = Modifier.weight(1f, true),
                    colors = androidx.compose.material3.ButtonDefaults.buttonColors(
                        containerColor = if (selectedGender == "남성") selectedColor else unselectedColor
                    )
                ) {
                    Text("남성")
                }

                Spacer(modifier = Modifier.width(16.dp))

                Button(
                    onClick = { selectedGender = "여성" },
                    shape = RoundedCornerShape(0.dp),
                    modifier = Modifier.weight(1f, true),
                    colors = androidx.compose.material3.ButtonDefaults.buttonColors(
                        containerColor = if (selectedGender == "여성") selectedColor else unselectedColor
                    )
                ) {
                    Text("여성")
                }
            }

            // 저장 버튼
            Button(
                onClick = {
                    val name= userName.ifEmpty { "Unknown" }
                    val gender = selectedGender ?: "Unknown"
                    navController.navigate("TaleList/$name/$gender")
                },
                shape = RoundedCornerShape(0.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("저장")
            }
        }
    }
}

