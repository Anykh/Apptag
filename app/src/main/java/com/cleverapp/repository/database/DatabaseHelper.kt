package com.cleverapp.repository.database

import com.cleverapp.repository.data.ImageTag
import com.cleverapp.repository.data.TaggedImage

//TODO use worker thread
class DatabaseHelper(val database: AppDatabase){

    fun getAllTaggedImages(): List<TaggedImage> {
        val images = database.taggedImagesDao().getAllTaggedImages()
        images.forEach {
            it.tags = getTags(it.id)
        }
        return images
    }

    private fun getTags(imageId: String): List<ImageTag> {
        return database.taggedImagesDao().getTags(imageId)
    }

    fun insertTaggedImage(taggedImage: TaggedImage) {
        database.taggedImagesDao().insertTaggedImage(taggedImage)
        insertImageTags(taggedImage.tags)
    }

    private fun insertImageTags(imageTags: List<ImageTag>) {
        database.taggedImagesDao().insertImageTags(imageTags)
    }

    fun deleteSavedImage(image: TaggedImage) {
        database.taggedImagesDao().deleteImageTags(image.tags)
        database.taggedImagesDao().deleteSavedImage(image)
    }

    fun deleteSavedImage(imageId: String) {
        database.taggedImagesDao().deleteImageTags(getTags(imageId))
        database.taggedImagesDao().deleteSavedImage(getTaggedImage(imageId))
    }

    fun getTaggedImage(imageId: String): TaggedImage {
        return database.taggedImagesDao().getTaggedImage(imageId)
                .also {it.tags = getTags(it.id)}
    }

    fun updateTaggedImages(imagesToUpdate: Collection<TaggedImage>) {
        database.taggedImagesDao().updateTaggedImages(imagesToUpdate)
    }

    fun updateImageTags(imageId: String, tags: List<ImageTag>) {
        database.taggedImagesDao().deleteImageTags(
                database.taggedImagesDao().getTags(imageId))
        insertImageTags(tags)
    }
}
