import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.checkpoint03_rm551534.ui.theme.Checkpoint03RM551534Theme

class EditHabitActivity : ComponentActivity() {

    lateinit var databaseHelper: DatabaseHelper
    var habitId: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        databaseHelper = DatabaseHelper(this)

        // Pega o habitId passado pela MainActivity
        habitId = intent.getIntExtra("habitId", 0)

        setContent {
            Checkpoint03RM551534Theme() {
                // Carregar e editar o hábito
                EditHabitScreen(
                    habitId = habitId,
                    databaseHelper = databaseHelper,
                    onSaveClick = {
                        // Salvar as mudanças no hábito
                        finish()
                    },
                    onDeleteClick = {
                        // Excluir o hábito e voltar à tela principal
                        databaseHelper.deleteHabit(habitId)
                        finish()
                    }
                )
            }
        }
    }
}

@Composable
fun EditHabitScreen(
    habitId: Int,
    databaseHelper: DatabaseHelper,
    onSaveClick: () -> Unit,
    onDeleteClick: () -> Unit
) {
    // Carregar os dados do hábito a partir do banco de dados
    var habitName by remember { mutableStateOf("") }
    var habitFrequency by remember { mutableStateOf("") }
    var habitDescription by remember { mutableStateOf("") }
    var habitReminder by remember { mutableStateOf("") }
    var habitStatus by remember { mutableStateOf(true) }

    LaunchedEffect(habitId) {
        val cursor = databaseHelper.getAllHabits()
        if (cursor.moveToFirst()) {
            do {
                if (cursor.getInt(cursor.getColumnIndexOrThrow(DatabaseHelper.COL_ID)) == habitId) {
                    habitName = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COL_NAME))
                    habitFrequency = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COL_FREQUENCY))
                    habitDescription = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COL_DESCRIPTION))
                    habitReminder = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COL_REMINDER))
                    habitStatus = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COL_STATUS)) == "ativo"
                    break
                }
            } while (cursor.moveToNext())
        }
    }

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
            // Atualiza o hábito no banco de dados
            val statusText = if (habitStatus) "ativo" else "inativo"
            databaseHelper.updateHabit(habitId, habitName, habitFrequency, habitDescription, habitReminder, statusText)
            onSaveClick()
        }) {
            Text(text = "Salvar Alterações")
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Botão para excluir o hábito
        Button(onClick = { onDeleteClick() }, colors = ButtonDefaults.buttonColors(MaterialTheme.colorScheme.error)) {
            Text(text = "Excluir Hábito")
        }
    }
}
