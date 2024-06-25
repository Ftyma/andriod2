package com.example.andriod2

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.amity.socialcloud.sdk.api.core.AmityCoreClient
import com.amity.socialcloud.sdk.api.core.endpoint.AmityEndpoint
import com.amity.socialcloud.sdk.core.session.AccessTokenRenewal
import com.amity.socialcloud.sdk.model.core.session.SessionHandler
import com.example.andriod2.ui.theme.Andriod2Theme
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.unit.dp

class MainActivity : ComponentActivity() {
    private  val loginViewModel : LoginViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        AmityCoreClient.setup(
            apiKey = "b0efe90c3bdda2304d628918520c1688845889e4bc363d2c",
            AmityEndpoint.CUSTOM(
                httpEndpoint = ("https://api.staging.amity.co/"),
                socketEndpoint = ("https://ssq.staging.amity.co"),
                mqttEndpoint = ("ssq.staging.amity.co"),
            )
        )


        setContent {
           LoginScreen(loginViewModel)
        }


    }
}

@Composable
fun LoginScreen(viewModel: LoginViewModel) {
    val loginState by viewModel.loginState.collectAsState()
    val sessionState by viewModel.sessionState.collectAsState()

    var userId by remember { mutableStateOf("tyma") }

    Column(
        modifier = Modifier.fillMaxSize().padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        TextField(
            value = userId,
            onValueChange = { userId = it },
            label = { Text("User ID") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(16.dp))
        Button(onClick = {
            viewModel.authenticate(getSessionHandler(),userId)
        }) {
            Text("Login")
        }
        Spacer(modifier = Modifier.height(16.dp))
        loginState?.let {
            Text(text = it)
        }

        sessionState?.let {
            Text(text = "Session State: ${it}")
        }
    }
}

@Composable
fun AuthenticationScreen(onAuthenticate: () -> Unit) {
    Button(onClick = { onAuthenticate() }) {
        Text(text = "Login")

    }
}

fun getSessionHandler(): SessionHandler {
    return object : SessionHandler {
        override fun sessionWillRenewAccessToken(renewal: AccessTokenRenewal) {
            renewal.renew()
        }
    }
}



