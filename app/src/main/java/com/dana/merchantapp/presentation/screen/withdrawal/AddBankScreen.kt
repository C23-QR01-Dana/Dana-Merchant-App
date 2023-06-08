package com.dana.merchantapp.presentation.screen.withdrawal

import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.DropdownMenuItem
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.ExposedDropdownMenuBox
import androidx.compose.material.ExposedDropdownMenuDefaults
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.LocalContentColor
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.material.TopAppBar
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.NavGraph.Companion.findStartDestination
import com.dana.merchantapp.data.model.BankAccount
import com.dana.merchantapp.presentation.ui.component.CustomTextField
import com.dana.merchantapp.presentation.ui.component.navigation.Screen
import com.dana.merchantapp.presentation.ui.theme.BluePrimary
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun AddBankScreen(navController: NavController, withdrawalViewModel: WithdrawalViewModel = hiltViewModel()) {
    val options = listOf("BCA", "Mandiri", "BRI", "BTPN")
    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()
    var expanded by remember { mutableStateOf(false) }
    var selectedOptionText by remember { mutableStateOf(options[0]) }
    var accountNumber by remember { mutableStateOf("") }
    var isNumberInvalid by remember { mutableStateOf(false) }
    var isComplete by remember { mutableStateOf(false) }
    val isBankAdded: Boolean by withdrawalViewModel.isBankAdded.collectAsState()

    if (isBankAdded && !isComplete) {
        isComplete = true
        navController.popBackStack()
    }

    Column() {
        TopAppBar(
            title = { Text("Add Bank Account") },
            navigationIcon = {
                IconButton(onClick = { navController.popBackStack() }) {
                    Icon(Icons.Filled.ArrowBack, contentDescription = null)
                }
            },
            modifier = Modifier
                .background(
                    brush = Brush.linearGradient(
                        colors = listOf(Color(0xFF86B0FF), Color(0xFF408CE2)),
                        start = Offset.Zero,
                        end = Offset.Infinite
                    )
                )
        )
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            ExposedDropdownMenuBox(
                expanded = expanded,
                onExpandedChange = {
                    expanded = !expanded
                },
                modifier = Modifier
                    .width(150.dp) // Adjust the width to your desired size
                    .padding(end = 8.dp)

            ) {
                TextField(
                    readOnly = true,
                    value = selectedOptionText,
                    onValueChange = { },
                    label = { Text("Bank") },
                    trailingIcon = {
                        ExposedDropdownMenuDefaults.TrailingIcon(
                            expanded = expanded
                        )
                    },
                    colors = ExposedDropdownMenuDefaults.textFieldColors(),
                    modifier = Modifier
                        .background(Color.White)
                )
                ExposedDropdownMenu(
                    expanded = expanded,
                    onDismissRequest = {
                        expanded = false
                    }
                ) {
                    options.forEach { selectionOption ->
                        DropdownMenuItem(
                            onClick = {
                                selectedOptionText = selectionOption
                                expanded = false
                                withdrawalViewModel.makeInvalid()
                            }
                        ) {
                            Text(text = selectionOption)
                        }
                    }
                }
            }
            CustomTextField(
                value = accountNumber,
                onValueChange = {
                    accountNumber = it
                    withdrawalViewModel.makeInvalid()
                                },
                label = "Account Number",
                isError = isNumberInvalid || !withdrawalViewModel.initialTrue.value,
                keyboardType = KeyboardType.Number
            )
        }
        if (!withdrawalViewModel.isAccountValid.value) {
            Button(
                onClick = {
                    val accountInt = accountNumber.toIntOrNull()
                    Log.v("accountInt", accountInt.toString())
                    if (accountInt == null) {
                        isNumberInvalid = true
                        coroutineScope.launch {
                            Toast.makeText(
                                context,
                                "Account Number Must Be Filled",
                                Toast.LENGTH_SHORT
                            )
                                .show()
                        }
                    } else if (accountNumber.length < 9) {
                        isNumberInvalid = true
                        coroutineScope.launch {
                            Toast.makeText(
                                context,
                                "Account Number Must Be More Than 9 Digit",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    } else {
                        isNumberInvalid = false
                        withdrawalViewModel.validateBank(selectedOptionText, accountNumber)
                    }
                },
                shape = RoundedCornerShape(20.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp)

            ) {
                Text(text = "Validate")
            }
        } else {
            Text(
                text = "Name: ${withdrawalViewModel.validAccount.value?.accountName ?: ""}",
                modifier = Modifier
                    .padding(bottom=16.dp, start = 16.dp, end = 16.dp),
                style = TextStyle(
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold
                ),
            )
            Button(
                onClick = {
                    if (withdrawalViewModel.validAccount.value != null) {
                        val account = BankAccount(
                            accountName = withdrawalViewModel.validAccount.value?.accountName ?: "",
                            bankAccountNo = withdrawalViewModel.validAccount.value?.bankAccountNo ?: "",
                            bankInst = withdrawalViewModel.validAccount.value?.bankInst ?: "")
                        withdrawalViewModel.addBank(account)
                    }
                },
                shape = RoundedCornerShape(20.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                colors = ButtonDefaults.buttonColors(
                    backgroundColor = Color.White,
                    contentColor = BluePrimary
                )
            ) {
                Text(
                    text = "Add Account",
                )
            }
        }
    }
}