package com.example.labappa9db.view

import android.content.Context
import android.util.Log
import android.widget.CheckBox
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.Checkbox
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.example.labappa9db.database.TodoController
import com.example.labappa9db.database.todo

/**
 * FininishedTodosScreen
 * UI Screen containing a Header with a back button and the List of finished Todos as cards
 * @param context the context from which the screen is called
 * @param navController the controller used to navigate from dashboard to here
 * @return null
 * */

@Composable
fun FinishedTodosScreen(
    context: Context, navController: NavHostController = rememberNavController()
){
    val todoController= TodoController(context)
    var todos by remember{ mutableStateOf(todoController.getFinishedTodos())}
    Column(modifier = Modifier.padding(16.dp)) {
        Spacer(modifier = Modifier.height(22.dp))
        Row(
            modifier = Modifier
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            IconButton(
                onClick = {
                    navController.navigate("dashboard")
                }) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = "Dashboard back icon"
                )
            }
            Text("Finished Todos", fontSize = 20.sp)
            }
        Spacer(modifier = Modifier.height(15.dp))
        LazyColumn(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(todos) { todo ->
                TodoItemCard(
                    item = todo,
                    onDeleteClick = {
                        todoController.deleteTodo(todo.id)
                        todos=todoController.getFinishedTodos()
                    },
                    onTickClick = {
                        todo.status=0
                        todoController.updateTodo(todo)
                        todos = todoController.getFinishedTodos()
                    }
                )
                Spacer(modifier= Modifier.height(10.dp))
                Log.d("Lazycolumn finished todos" ,"Todo Item Nr. ${todo.id} added, called ${todo.name} with status ${todo.status}")
            }
        }
    }
}

/**
 * TodoItemCard
 * the Card designed for the finished todos
 * these do not allow editing, as they were completed I didnt think that would be necessary
 * you can uncheck them again to move them in the other section or delete them
 * @param item the todo item
 * @param onDeleteClick the function called when pressing the delete button on the card
 * @param onTickClick the function called when unticking the item
 * @return null
 * */

@Composable
fun TodoItemCard(item: todo, onDeleteClick: (todo)-> Unit, onTickClick:(todo)-> Unit){
    var checked by remember{ mutableStateOf(true)}
    Row(horizontalArrangement = Arrangement.SpaceBetween, modifier= Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically){
        Text(text= item.name, textAlign= TextAlign.Left)
        IconButton({onTickClick(item)}, modifier = Modifier.width(150.dp)) {
            Row() {
                Text("Finished")
                Checkbox(checked, onCheckedChange={onTickClick(item)})
            }
        }
        Icon(imageVector= Icons.Default.Delete, tint= Color.Red, contentDescription="Delete Todo", modifier = Modifier.clickable{onDeleteClick(item)})
    }
}