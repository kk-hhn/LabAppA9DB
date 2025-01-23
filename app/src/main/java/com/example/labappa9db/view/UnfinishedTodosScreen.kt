package com.example.labappa9db.view

import android.content.Context
import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.Checkbox
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.example.labappa9db.database.TodoController
import com.example.labappa9db.database.todo

/**
 * UnfininishedTodosScreen
 * UI Screen containing a Header with a back button, the List of unfinished Todos as cards and a Create Button in the Footing
 * @param context the context from which the screen is called
 * @param navController the controller used to navigate from dashboard to here
 * @return null
 * */

@Composable
fun UnfinishedTodosScreen(
    context: Context, navController: NavHostController = rememberNavController()
){
    val todoController= TodoController(context)
    var todos by remember{ mutableStateOf(todoController.getUnfinishedTodos())}
    var selectedTodo by remember{ mutableStateOf<todo?>(null)}
    var editOpened by remember{ mutableStateOf(false)}
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
            Text("Unfinished Todos", fontSize = 20.sp)

        }

        //Lazycolumn containing all unfinished todos as cards, clickable to open the edit dialog
        //as well as tick-able and delete-able from outside the dialog
        LazyColumn(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(todos) { todo ->
                UnfinishedTodoItemCard(
                    item = todo,
                    onClick = {
                        selectedTodo = todo
                        editOpened = true
                    },
                    onDeleteClick = {
                        todoController.deleteTodo(todo.id)
                        todos= todoController.getUnfinishedTodos()
                    },
                    onChecked = {
                        if(todo.status==0){
                            todo.status=1
                        }else{
                            todo.status=0
                        }
                        todoController.updateTodo(todo)
                        todos= todoController.getUnfinishedTodos()
                    }
                )
                Spacer(modifier= Modifier.height(10.dp))
                Log.d("Lazycolumn unfinished todos" ,"Todo Item Nr. ${todo.id} added, called ${todo.name} with status ${todo.status}")
            }
        }
        Spacer(modifier = Modifier.height(22.dp))
        IconButton(onClick={selectedTodo=null;editOpened=true}, Modifier.background(Color.LightGray,
            RoundedCornerShape(5.dp))){
            Icon(imageVector= Icons.Default.Add, contentDescription = "Add Todo Button")
        }
    }
    if (editOpened) {
        TodoEditDialog(
            todo = selectedTodo,
            onDismiss = { editOpened = false },
            onSave = { todo ->
                if (todo.id == 0) {
                    todoController.addTodo(todo)
                } else {
                    todoController.updateTodo(todo)
                }
                todos = todoController.getUnfinishedTodos()
                editOpened = false
            },
            onFail={Toast.makeText(context, "The todos name and a priority must be picked!", Toast.LENGTH_SHORT).show()}
        )
    }
}

@Composable
fun UnfinishedTodoItemCard(item: todo,onDeleteClick:(todo)->Unit, onClick:()-> Unit, onChecked:(todo)-> Unit) {
    var todoFinished by remember{ mutableStateOf(false)}
    if(item.status==0){todoFinished=false}else{todoFinished=true}
    Card(modifier = Modifier.clickable{onClick()}.fillMaxWidth()) {
        Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.SpaceBetween, modifier = Modifier.fillMaxWidth()) {
            Text("${item.name}   Priority: ${item.priority}", textAlign = TextAlign.Left)
            Checkbox(todoFinished,
                onCheckedChange = { todoFinished = !todoFinished; onChecked(item) })
            Icon(imageVector= Icons.Default.Delete, tint= Color.Red, contentDescription="Delete Todo", modifier = Modifier.clickable{onDeleteClick(item)})
        }
    }
}
/**
 * TodoEditDialog
 * Dialog to allow the user to edit or create a new todo
 * @param todo the selected todo if it exists
 * @param onDismiss the function executed when clicking outside the dialog or the cancel button
 * @param onSave the function executed when saving the todo
 * @return null
 * */

@Composable
fun TodoEditDialog(
    todo: todo?,
    onDismiss: () -> Unit,
    onSave: (todo) -> Unit,
    onFail: ()->Unit
) {
    var name by remember { mutableStateOf(todo?.name?:"") }
    var priority by remember { mutableIntStateOf(todo?.priority?:0) }
    var description by remember { mutableStateOf(todo?.description?:"") }
    var status by remember { mutableIntStateOf(todo?.status?:0) }
    var deadline by remember{ mutableStateOf(todo?.deadline?:"")}
    var openedDropDown by remember{mutableStateOf(false)}
    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Text(text = if (todo == null) "Create new Todo" else "Edit Todo")
        },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                TextField(
                    value = name,
                    onValueChange = { name = it },
                    label = { Text("Name of the Todo") },
                    modifier = Modifier.fillMaxWidth()
                )
                Text(text="Priority: $priority", modifier = Modifier
                    .clickable { openedDropDown = !openedDropDown }
                    .padding(20.dp)
                    .background(Color(169,169,169, 69))
                    .fillMaxWidth())
                DropdownMenu(offset= DpOffset(0.dp,0.dp),expanded= openedDropDown, onDismissRequest = {openedDropDown=false}) {
                    DropdownMenuItem(text={Text("1")},
                        onClick={priority=1; openedDropDown=false})
                    DropdownMenuItem(text={Text("2")},
                        onClick={priority=2; openedDropDown=false})
                    DropdownMenuItem(text={Text("3")},
                        onClick={priority=3; openedDropDown=false})
                }
                TextField(
                    value = description,
                    onValueChange = { description = it },
                    label = { Text("Description") },
                    modifier = Modifier.fillMaxWidth()
                )
                TextField(
                    value = deadline,
                    onValueChange = { deadline = it },
                    label = { Text("Deadline for the Item") },
                    modifier = Modifier.fillMaxWidth()
                )
                if(todo!=null){
                    var checked by remember{mutableStateOf(false)}
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text("Finished")
                        Checkbox(checked, onCheckedChange = {checked=!checked
                        if(checked){
                            status= 1
                        }else{
                            status=0
                        }
                        })
                    }
                }
            }
        },
        confirmButton = {
            Button(onClick = {
                val updatedTodo = todo(
                    id = todo?.id ?: 0,
                    name = name,
                    description = description,
                    priority = priority,
                    status = status,
                    deadline= deadline
                )
                if(name!=""&&priority!=0){
                    Log.d("onSave Dialog", "updated todo $name with completed: $status")
                    onSave(updatedTodo)
                }
                else{
                    onFail()
                }
            }) {
                Text("Save Todo")
            }
        },
        dismissButton = {
            Button(onClick = {onDismiss()}){
                Text("Cancel")
            }

        }
    )
}