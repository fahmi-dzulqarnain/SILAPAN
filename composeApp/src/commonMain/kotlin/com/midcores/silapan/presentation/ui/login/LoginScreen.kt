package com.midcores.silapan.presentation.ui.login

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Color.Companion.White
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.midcores.silapan.presentation.ui.component.SilapanHeader
import org.jetbrains.compose.resources.painterResource
import org.koin.compose.koinInject
import silapan.composeapp.generated.resources.Res
import silapan.composeapp.generated.resources.silapan_logo

@Composable
fun LoginScreen(
    onLoginSuccess: () -> Unit = {}
) {
    val viewModel: LoginViewModel = koinInject()
    val state by viewModel.uiState.collectAsState()

    Box(
        modifier = Modifier
            .fillMaxSize()
            .imePadding()
            .background(
                Brush.linearGradient(
                    colors = listOf(Color(0xFF4C8479), Color(0xFF2B5F56)),
                    start = Offset(0f, 0f),
                    end = Offset(1200f, 1200f)
                )
            )
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            LazyColumn(
                modifier = Modifier
                    .padding(24.dp)
                    .weight(1f),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                item {

                    Image(
                        painter = painterResource(Res.drawable.silapan_logo),
                        contentDescription = "SILAPAN Logo",
                        modifier = Modifier
                            .width(300.dp)
                            .height(300.dp)
                            .padding(top = 16.dp)
                    )

                    SilapanHeader(
                        isShowImage = false,
                        subtitleColor = White
                    )

                    Text(
                        "Kecamatan Nongsa - Kota Batam",
                        style = MaterialTheme.typography.headlineMedium.copy(color = White),
                        modifier = Modifier.padding(top = 16.dp)
                    )

                    Spacer(modifier = Modifier.height(32.dp))

                    OutlinedTextField(
                        value = state.phone,
                        textStyle = MaterialTheme.typography.bodyMedium.copy(
                            color = White
                        ),
                        onValueChange = { viewModel.onPhoneChanged(it) },
                        label = {
                            Text("No HP", color = White.copy(alpha = 0.6f))
                        },
                        placeholder = {
                            Text("No HP", color = White.copy(alpha = 0.6f))
                        },
                        leadingIcon = {
                            Icon(
                                imageVector = Icons.Default.Person,
                                contentDescription = null,
                                modifier = Modifier
                                    .size(24.dp)
                                    .padding(start = 6.dp),
                                tint = White,
                            )
                        },
                        singleLine = true,
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone),
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(Color.Transparent),
                        shape = RoundedCornerShape(24.dp),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = White,
                            unfocusedBorderColor = White.copy(alpha = 0.5f),
                            focusedContainerColor = Color.Transparent,
                            unfocusedContainerColor = Color.Transparent
                        )
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    OutlinedTextField(
                        value = state.password,
                        textStyle = MaterialTheme.typography.bodyMedium.copy(
                            color = White
                        ),
                        onValueChange = { viewModel.onPasswordChanged(it) },
                        label = {
                            Text("Password", color = White.copy(alpha = 0.6f))
                        },
                        placeholder = {
                            Text("Password", color = White.copy(alpha = 0.6f))
                        },
                        leadingIcon = {
                            Icon(
                                imageVector = Icons.Default.Lock,
                                contentDescription = null,
                                modifier = Modifier
                                    .size(24.dp)
                                    .padding(start = 6.dp),
                                tint = White,
                            )
                        },
                        singleLine = true,
                        visualTransformation = PasswordVisualTransformation(),
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(Color.Transparent),
                        shape = RoundedCornerShape(24.dp),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = White,
                            unfocusedBorderColor = White.copy(alpha = 0.5f),
                            focusedContainerColor = Color.Transparent,
                            unfocusedContainerColor = Color.Transparent
                        )
                    )
                }
            }

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(150.dp)
                    .clip(RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp))
                    .background(White)
                    .padding(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Button(
                    onClick = { viewModel.login() },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(48.dp),
                    enabled = !state.isLoading
                ) {
                    if (state.isLoading) {
                        CircularProgressIndicator(
                            color = MaterialTheme.colorScheme.onPrimary,
                            strokeWidth = 2.dp,
                            modifier = Modifier.size(20.dp)
                        )
                    } else {
                        Text(
                            "LOGIN",
                            style = MaterialTheme.typography.headlineMedium
                        )
                    }
                }

                state.error?.let { errorMsg ->
                    Spacer(modifier = Modifier.height(16.dp))
                    Text(
                        text = errorMsg,
                        color = MaterialTheme.colorScheme.error,
                        textAlign = TextAlign.Center,
                        style = MaterialTheme.typography.bodyMedium
                    )
                }

                if (state.success) {
                    onLoginSuccess()
                }
            }
        }
    }

}
