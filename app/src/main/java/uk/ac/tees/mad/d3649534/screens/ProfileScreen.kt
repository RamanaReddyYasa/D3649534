package uk.ac.tees.mad.d3649534.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ContactSupport
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.ArrowBackIosNew
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import uk.ac.tees.mad.d3649534.R
import uk.ac.tees.mad.d3649534.data.domain.UserData
import uk.ac.tees.mad.d3649534.navigation.NavigationDestination
import uk.ac.tees.mad.d3649534.screens.components.AlertDialog
import uk.ac.tees.mad.d3649534.ui.theme.green

object ProfileDestination : NavigationDestination {
    override val route = "profile"
    override val titleRes: Int = R.string.app_name
}

@Composable
fun ProfileScreen(
    currentUser: UserData?,
    onSignOut: () -> Unit,
    onSignIn: () -> Unit,
    onNavigateUp: () -> Unit,
    onNavigationHistory: () -> Unit
) {
    var signOutConfirm by remember {
        mutableStateOf(false)
    }
    var isTimerRunning by remember {
        mutableStateOf(false)
    }

    Column(
        modifier = Modifier
            .fillMaxSize(),
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .background(green)
        ) {
            Spacer(modifier = Modifier.height(4.dp))
            IconButton(
                onClick = onNavigateUp
            ) {
                Icon(
                    imageVector = Icons.Default.ArrowBackIosNew,
                    contentDescription = "Go back",
                    tint = Color.White
                )
            }
            Row(
                Modifier
                    .fillMaxWidth()
                    .padding(20.dp),
                horizontalArrangement = Arrangement.Center
            ) {
                Text(
                    text = "Profile",
                    style = TextStyle(fontSize = 24.sp, fontWeight = FontWeight.Bold)
                )
            }
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.Center
                ) {

                    Icon(
                        imageVector = Icons.Default.AccountCircle,
                        contentDescription = "User Icon",
                        modifier = Modifier.size(100.dp),
                    )
                }
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = if (currentUser == null) "Guest" else "${currentUser.username}",
                    style = TextStyle(
                        color = Color.White,
                        fontSize = 20.sp,
                        letterSpacing = 0.5.sp,
                        fontWeight = FontWeight.W500
                    ),
                )
                Spacer(modifier = Modifier.height(6.dp))
                Text(
                    text = if (currentUser == null) "guest@gmail.com" else "${currentUser.email}",
                    style = TextStyle(
                        color = Color.White,
                        fontSize = 16.sp,
                        letterSpacing = 0.5.sp,
                    ),
                )
            }
            HorizontalDivider()
        }
        Column(modifier = Modifier.padding(24.dp)) {
            if (currentUser != null) {
                AccountListCard(
                    icon = Icons.Filled.History,
                    text = "Reminder History",
                    onClick = onNavigationHistory
                )
            } else {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(MaterialTheme.shapes.large)
                        .background(MaterialTheme.colorScheme.primary.copy(alpha = .2f))
                ) {
                    Column(modifier = Modifier.padding(12.dp)) {
                        Text(
                            text = "Log in to save your data.",
                            style = MaterialTheme.typography.bodyMedium,
                            modifier = Modifier.padding(12.dp)
                        )
                        SignButton(
                            text = "Log in",
                            onClick = {
                                onSignIn()
                            },
                            isTimerRunning = isTimerRunning,
                            modifier = Modifier.padding(
                                bottom = 12.dp,
                                start = 12.dp,
                                end = 12.dp
                            ),
                            color = MaterialTheme.colorScheme.primary
                        )
                    }
                }
            }
            AccountListCard(icon = Icons.Filled.Settings, text = "Preferences", onClick = {})
            AccountListCard(
                icon = Icons.AutoMirrored.Filled.ContactSupport,
                text = "Support",
                onClick = {}
            )
            Spacer(modifier = Modifier.weight(1f))
            AnimatedVisibility(visible = currentUser != null) {
                SignButton(
                    text = "Sign out",
                    onClick = {
                        if (currentUser != null) {
                            signOutConfirm = true
                        }
                    },
                    isTimerRunning = isTimerRunning,
                    color = Color.Red.copy(alpha = 0.5f)
                )
            }
        }


        AnimatedVisibility(visible = signOutConfirm) {
            AlertDialog(
                onDismissRequest = {
                    signOutConfirm = false
                    isTimerRunning = false
                },
                onConfirmation = {
                    isTimerRunning = true
                    onSignOut()
                },
                dialogTitle = "",
                dialogText = "Are you sure you want to sign out?"
            )
        }
    }
}

@Composable
fun AccountListCard(icon: ImageVector, text: String, onClick: () -> Unit) {

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .height(64.dp)
            .clickable { onClick() }
    ) {
        Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.weight(1f)) {
            Icon(imageVector = icon, contentDescription = null)
            Spacer(modifier = Modifier.width(12.dp))
            Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                Text(text = text)
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.KeyboardArrowRight,
                    contentDescription = "Click here"
                )
            }
        }
        HorizontalDivider()
    }
}

@Composable
fun SignButton(
    text: String,
    onClick: () -> Unit,
    isTimerRunning: Boolean,
    modifier: Modifier = Modifier,
    color: Color
) {

    OutlinedButton(
        onClick = {
            onClick()
        },
        shape = RoundedCornerShape(16.dp),
        modifier = modifier
            .fillMaxWidth()
            .height(52.dp),
        border = BorderStroke(1.7.dp, color)
    ) {
        if (isTimerRunning)
            CircularProgressIndicator(
                color = color
            )
        else
            Text(text = text, color = color)
    }
}

@Preview
@Composable
private fun ProfileView() {
    ProfileScreen(
        currentUser = null,
        onSignOut = { /*TODO*/ },
        onNavigateUp = {},
        onSignIn = {},
        onNavigationHistory = {})
}