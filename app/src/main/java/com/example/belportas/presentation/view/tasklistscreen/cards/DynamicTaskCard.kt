package com.example.belportas.presentation.view.tasklistscreen.cards

import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberUpdatedState
import com.example.belportas.data.DeliveryStatus
import com.example.belportas.data.Task
import com.example.belportas.model.TaskViewModel
import com.example.belportas.presentation.view.tasklistscreen.cards.AcceptTaskCard
import com.example.belportas.presentation.view.tasklistscreen.cards.DeletedCard
import com.example.belportas.presentation.view.tasklistscreen.cards.DoneTaskCard
import com.example.belportas.presentation.view.tasklistscreen.cards.TaskCard

@Composable
fun DynamicTaskCard(
    task: Task,
    isDetailsVisible: MutableState<Boolean>,
    taskViewModel: TaskViewModel,
    editTaskScreen:()->Unit
) {
    val updatedTaskState by rememberUpdatedState(task)

    when (updatedTaskState.deliveryStatus) {
        DeliveryStatus.PEDIDO_SEPARADO -> { AcceptTaskCard(updatedTaskState,taskViewModel) }
        DeliveryStatus.PEDIDO_EM_TRANSITO -> { TaskCard(updatedTaskState, isDetailsVisible,
            taskViewModel,editTaskScreen)
        }
        DeliveryStatus.PEDIDO_EXCLUIDO -> { DeletedCard(updatedTaskState,taskViewModel)
        }
        else -> { DoneTaskCard(updatedTaskState)
        }
    }
}