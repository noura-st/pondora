package com.example.pondora.data.db

import com.example.pondora.data.db.entity.TaskEntity
import com.example.pondora.data.db.entity.UserEntity
import com.example.pondora.data.repository.TaskRepository
import com.example.pondora.data.repository.UserRepository
import kotlinx.coroutines.flow.first

object TaskSeeder {

    suspend fun seedIfEmpty(
        taskRepository: TaskRepository,
        userRepository: UserRepository
    ) {
        val existingUser = userRepository.getUser().first()
        if (existingUser == null) {
            userRepository.insertUser(UserEntity())
        }

        if (taskRepository.getTaskCount() > 0) return

        val tasks = listOf(
            TaskEntity(title = "Görevliye 'kolay gelsin' de", description = "Kantinde veya kafeteryada görevliye gülümseyerek 'kolay gelsin' de.", difficulty = 1, category = "selamlama"),
            TaskEntity(title = "Birine saati sor", description = "Yolda veya okulda birine saati sor.", difficulty = 1, category = "soru_sorma"),
            TaskEntity(title = "Komşuna selam ver", description = "Bugün karşılaştığın bir komşuna gülümseyerek selam ver.", difficulty = 1, category = "selamlama"),
            TaskEntity(title = "Kasiyere teşekkür et", description = "Market veya kafede kasiyere teşekkür et.", difficulty = 1, category = "tesekkur"),
            TaskEntity(title = "Otobüste teşekkür et", description = "Otobüsten inerken şoföre teşekkür et.", difficulty = 1, category = "tesekkur"),
            TaskEntity(title = "Bir mağazada ürün sor", description = "Çalışana bir ürünün yerini sor.", difficulty = 1, category = "soru_sorma"),
            TaskEntity(title = "Bir arkadaşına ilk mesajı sen at", description = "Bugün bir arkadaşına konuşmayı sen başlat.", difficulty = 1, category = "iletisim"),

            TaskEntity(title = "Bir yabancıya yol tarifi ver", description = "Birisi senden yol tarifi isterse yardım et.", difficulty = 2, category = "yardim"),
            TaskEntity(title = "Kafede siparişini sesli ver", description = "Kafede siparişini net ve anlaşılır bir sesle ver.", difficulty = 2, category = "siparis"),
            TaskEntity(title = "Sınıfta bir soru sor", description = "Derste aklına takılan bir şeyi hocaya veya arkadaşına sor.", difficulty = 2, category = "soru_sorma"),
            TaskEntity(title = "Kısa bir sohbet başlat", description = "Tanıdığın biriyle en az 1 dakikalık sohbet et.", difficulty = 2, category = "sohbet"),
            TaskEntity(title = "Topluluk içinde fikir belirt", description = "Bir grup konuşmasında fikrini söyle.", difficulty = 2, category = "grup"),
            TaskEntity(title = "Telefonla bir bilgi al", description = "Bir yeri telefonla arayıp bilgi sor.", difficulty = 2, category = "telefon"),

            TaskEntity(title = "Yeni biriyle tanış", description = "Bugün tanımadığın biriyle küçük bir sohbet başlat.", difficulty = 3, category = "tanisma"),
            TaskEntity(title = "Grup sohbetine katıl", description = "Bir grup konuşmasına katıl ve en az bir şey söyle.", difficulty = 3, category = "grup"),
            TaskEntity(title = "Yeni bir arkadaş edin", description = "Bugün yeni biriyle tanışıp sohbet etmeye çalış.", difficulty = 3, category = "tanisma"),
            TaskEntity(title = "Topluluk önünde konuş", description = "Kalabalık bir ortamda gönüllü olarak konuş.", difficulty = 3, category = "topluluk"),
            TaskEntity(title = "Bir etkinliğe tek başına katıl", description = "Tek başına sosyal bir etkinliğe git.", difficulty = 3, category = "sosyallesme")
        )

        tasks.forEach { taskRepository.insertTask(it) }
    }
}