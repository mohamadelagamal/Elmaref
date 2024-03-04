package com.elmaref.data.model.quran.api

import com.google.gson.annotations.SerializedName

data class Ayah(

	@field:SerializedName("audio_files")
	val audioFiles: List<AudioFilesItem?>? = null,

	@field:SerializedName("pagination")
	val pagination: Pagination? = null
)

data class Pagination(

	@field:SerializedName("next_page")
	val nextPage: Any? = null,

	@field:SerializedName("per_page")
	val perPage: Int? = null,

	@field:SerializedName("total_records")
	val totalRecords: Int? = null,

	@field:SerializedName("total_pages")
	val totalPages: Int? = null,

	@field:SerializedName("current_page")
	val currentPage: Int? = null
)

data class AudioFilesItem(

	@field:SerializedName("verse_key")
	val verseKey: String? = null,

	@field:SerializedName("url")
	val url: String? = null
)
