package com.example.checkpoint03_rm551534  // Certifique-se de que o pacote corresponde ao caminho do arquivo

import AddHabitActivity
import DatabaseHelper
import EditHabitActivity
import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

class MainActivity : ComponentActivity() {

    lateinit var databaseHelper: DatabaseHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Inicializa o banco de dados
        databaseHelper = DatabaseHelper(this)

        setContent {
            MaterialTheme {
                // Tela principal com a lista de hábitos
                HabitListScreen(
                    databaseHelper = databaseHelper,
                    onAddHabitClick = {
                        // Abrir AddHabitActivity
                        startActivity(Intent(this, AddHabitActivity::class.java))
                    },
                    onEditHabitClick = { habitId ->
                        // Abrir EditHabitActivity com o ID do hábito
                        val intent = Intent(this, EditHabitActivity::class.java)
                        intent.putExtra("habitId", habitId)
                        startActivity(intent)
                    }
                )
            }
        }
    }
}

@Composable
fun HabitListScreen(
    databaseHelper: DatabaseHelper,
    onAddHabitClick: () -> Unit,
    onEditHabitClick: (Int) -> Unit
) {
    // Obter hábitos do banco de dados
    val habitList = remember { mutableStateListOf<Habit>() }

    // Carregar os hábitos do banco de dados
    LaunchedEffect(Unit) {
        val cursor = databaseHelper.getAllHabits()
        if (cursor.moveToFirst()) {
            do {
                val id = cursor.getInt(cursor.getColumnIndexOrThrow(DatabaseHelper.COL_ID))
                val name = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COL_NAME))
                val frequency = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COL_FREQUENCY))
                val description = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COL_DESCRIPTION))
                val reminder = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COL_REMINDER))
                val status = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COL_STATUS))
                habitList.add(Habit(id, name, frequency, description, reminder, status))
            } while (cursor.moveToNext())
        }
    }

    Scaffold(
        floatingActionButton = {
            FloatingActionButton(onClick = onAddHabitClick) {
                Icon(imageVector = Icons.Default.Add, contentDescription = "Adicionar Hábito")
            }
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp)
        ) {
            Text(text = "Seus Hábitos", style = MaterialTheme.typography.titleLarge)

            Spacer(modifier = Modifier.height(16.dp))

            LazyColumn(modifier = Modifier.fillMaxWidth()) {
                items(habitList.size) { index ->
                    HabitItem(
                        habit = habitList[index],
                        onEditHabitClick = onEditHabitClick
                    )
                }
            }
        }
    }
}

@Composable
fun HabitItem(habit: Habit, onEditHabitClick: (Int) -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column {
                Text(text = habit.name)
                Text(text = habit.frequency)
            }
            Button(onClick = { onEditHabitClick(habit.id) }) {
                Text(text = "Editar")
            }
        }
    }
}
