package com.example.talevoice.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.SegmentedButton
import androidx.compose.material3.SegmentedButtonDefaults
import androidx.compose.material3.SingleChoiceSegmentedButtonRow
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.example.talevoice.viewmodel.UserInfoViewModel


@Composable
fun UserInfoScreen(
    navController: NavHostController,
    userInfoViewModel: UserInfoViewModel = viewModel()
) {

    val name = userInfoViewModel.name
    val gender = userInfoViewModel.gender

    val unselectedColor = MaterialTheme.colorScheme.primaryContainer
    val selectedColor = MaterialTheme.colorScheme.primary

    var selectedIndex by remember { mutableIntStateOf(0) }
    val options = listOf("남성", "여성")

    // 저장 버튼 활성화 조건: 이름과 성별이 모두 입력된 경우
    val isSaveEnabled = name.isNotEmpty() && gender.isNotEmpty()



    Box(
        modifier = Modifier
            .fillMaxSize()
            .imePadding() // Box 전체에 적용
            .padding(16.dp)
    ) {
        Column(
            modifier = Modifier.fillMaxWidth(),
            verticalArrangement = Arrangement.Center
        ) {

            // 안내 문구
            Text(
                text = "이름을 입력해주세요",
                fontFamily = FontFamily.Serif,
                fontWeight = FontWeight.Thin,
                style = MaterialTheme.typography.headlineSmall,
                modifier = Modifier.padding(bottom = 20.dp)
            )

            // 입력 필드
            OutlinedTextField(
                value = name,
                onValueChange = { newName ->
                    if (newName.length <= 10) {
                        userInfoViewModel.onNameChanged(newName)
                    }
                },
                textStyle = TextStyle(
                    fontFamily = FontFamily.Serif,
                    fontSize = 17.sp, // 원하는 폰트 크기 설정
                    fontWeight = FontWeight.Normal
                ),

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
                    Text("${name.length} / 10")
                },
                singleLine = true,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 15.dp)
            )



            SingleChoiceSegmentedButtonRow(
                modifier = Modifier
                    .width(LocalConfiguration.current.screenWidthDp.dp.times(0.75f))
                    .wrapContentHeight()
                    .align(Alignment.CenterHorizontally)
                    .padding(bottom = 20.dp)
            ) {
                options.forEachIndexed { index, label ->
                    SegmentedButton(
                        shape = SegmentedButtonDefaults.itemShape(
                            index = index,
                            count = options.size
                        ),
                        onClick = {
                            selectedIndex = index
                            userInfoViewModel.onGenderChanged(label)
                        },
                        selected = index == selectedIndex,
                        label = { Text(label, fontFamily = FontFamily.Serif, fontSize = 16.sp) }
                    )
                }
            }

        }

        Button(
            onClick = {
                navController.navigate("TaleList/${userInfoViewModel.name}/${userInfoViewModel.gender}")
            },
            enabled = isSaveEnabled, // 이름과 성별이 모두 입력된 경우 활성화
            colors = androidx.compose.material3.ButtonDefaults.buttonColors(
                containerColor = if (isSaveEnabled) selectedColor else unselectedColor,
                contentColor = if (isSaveEnabled) MaterialTheme.colorScheme.onPrimary else MaterialTheme.colorScheme.onSurface
            ),
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.BottomCenter)
                .imePadding() // 키보드가 나타나면 적절히 위로 이동

        ) {
            Text("저장", fontFamily = FontFamily.Serif, fontSize = 20.sp)
        }
    }
}

