package com.stj.fb2site.takeout

import com.stj.fb2site.model.Post
import org.json.JSONArray
import java.nio.file.Files
import java.nio.file.Path

class TakeoutParser {

    fun parse(root: Path): List<Post> {

        val posts = mutableListOf<Post>()

        val postsDir =
            root.resolve("your_facebook_activity")
                .resolve("posts")

        if (!Files.exists(postsDir)) {
            return posts
        }

        Files.list(postsDir)
            .filter {
                it.fileName.toString()
                    .startsWith("your_posts__check_ins__photos_and_videos")
            }
            .sorted()
            .forEach { file ->

                val json =
                    Files.readString(file)

                val array =
                    JSONArray(json)

                for (i in 0 until array.length()) {
                    posts.add(
                        parsePost(
                            array.getJSONObject(i)
                        )
                    )
                }
            }

        return posts
    }
}
