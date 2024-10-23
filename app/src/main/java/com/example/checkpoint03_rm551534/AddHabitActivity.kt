import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.checkpoint03_rm551534.ui.theme.Checkpoint03RM551534Theme

class AddHabitActivity : ComponentActivity() {

    lateinit var databaseHelper: DatabaseHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            databaseHelper = DatabaseHelper(this)

            Checkpoint03RM551534Theme() {
                AddHabitScreen(
                    onSaveClick = { habitName, habitFrequency, habitDescription, habitReminder, habitStatus ->
                        // Insere o novo hábito no banco de dados
                        databaseHelper.insertHabit(habitName, habitFrequency, habitDescription, habitReminder, habitStatus)
                        finish()
                    }
                )
            }
        }
    }
}

@Composable
fun AddHabitScreen(
    onSaveClick: (String, String, String, String, String) -> Unit
) {
    var habitName by remember { mutableStateOf("") }
    var habitFrequency by remember { mutableStateOf("") }
    var habitDescription by remember { mutableStateOf("") }
    var habitReminder by remember { mutableStateOf("") }
    var habitStatus by remember { mutableStateOf(true) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        TextField(value = habitName, onValueChange = { habitName = it }, label = { Text("Nome do Hábito") })
        Spacer(modifier = Modifier.height(8.dp))
        TextField(value = habitFrequency, onValueChange = { habitFrequency = it }, label = { Text("Frequência") })
        Spacer(modifier = Modifier.height(8.dp))
        TextField(value = habitDescription, onValueChange = { habitDescription = it }, label = { Text("Descrição") })
        Spacer(modifier = Modifier.height(8.dp))
        TextField(value = habitReminder, onValueChange = { habitReminder = it }, label = { Text("Lembrete") })
        Spacer(modifier = Modifier.height(8.dp))
        Switch(checked = habitStatus, onCheckedChange = { habitStatus = it }, modifier = Modifier.padding(8.dp))
        Spacer(modifier = Modifier.height(16.dp))
        Button(onClick = {
            val statusText = if (habitStatus) "ativo" else "inativo"
            onSaveClick(habitName, habitFrequency, habitDescription, habitReminder, statusText)
        }) {
            Text(text = "Salvar Hábito")
        }
    }
}
