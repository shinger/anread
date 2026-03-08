<template>
  <div class="w-full h-full fixed top-0 left-0 bg-gray-50 bg-opacity-50 z-100">
    <div class="w-full h-6 my-4 flex justify-center text-xl font-medium">
      点评 《{{ bookInfo.title }}》
    </div>
    <div class="flex flex-row h-16 mx-8 pb-4 border-b border-gray-300">
      <span class="mr-4 text-lg flex items-center mt-1">评分</span>
      <Rate @update-score="update" :score="score" theme="yellow" />
      <span class="ml-4 text-lg flex items-center mt-1">{{ score }}分</span>
    </div>
    <div class="flex flex-row justify-between items-center mx-4 my-2">
      <button
        class="w-24 h-8 bg-gray-200 text-gray-600 rounded-md"
        @click="setRecommendation(1)"
        :class="{ 'border border-[#bd763c]': recommendation === 1 }"
      >
        推荐
      </button>
      <button
        class="w-24 h-8 bg-gray-200 text-gray-600 rounded-md"
        @click="setRecommendation(2)"
        :class="{ 'border border-[#bd763c]': recommendation === 2 }"
      >
        一般
      </button>
      <button
        class="w-24 h-8 bg-gray-200 text-gray-600 rounded-md"
        @click="setRecommendation(3)"
        :class="{ 'border border-[#bd763c]': recommendation === 3 }"
      >
        不行
      </button>
    </div>
    <div class="flex flex-row h-100 mx-8 pb-4 border-gray-300">
      <textarea
        class="w-full h-48 mt-2 border border-gray-300 dark:border-gray-600 rounded-md text-gray-800 dark:text-gray-200 bg-white dark:bg-black"
        placeholder="点评这本书..."
        v-model="comment"
      ></textarea>
    </div>
    <div class="border-t border-gray-300 fixed bottom-0 w-full pt-4">
      <div
        class="flex flex-row w-16 h-6 bg-gray-200 rounded-lg cursor-pointer text-sm mx-6 justify-center items-center"
        @click="isPublic = !isPublic"
        v-show="isPublic"
      >
        <svg
          xmlns="http://www.w3.org/2000/svg"
          xmlns:xlink="http://www.w3.org/1999/xlink"
          viewBox="0 0 24 24"
          width="20"
          height="20"
        >
          <g
            fill="none"
            stroke="currentColor"
            stroke-width="2"
            stroke-linecap="round"
            stroke-linejoin="round"
          >
            <path d="M12 12v.01"></path>
            <path d="M14.828 9.172a4 4 0 0 1 0 5.656"></path>
            <path d="M17.657 6.343a8 8 0 0 1 0 11.314"></path>
            <path d="M9.168 14.828a4 4 0 0 1 0-5.656"></path>
            <path d="M6.337 17.657a8 8 0 0 1 0-11.314"></path>
          </g></svg
        >公开>
      </div>
      <div
        class="flex flex-row w-16 h-6 bg-gray-200 rounded-lg cursor-pointer text-sm mx-6 justify-center items-center"
        @click="isPublic = !isPublic"
        v-show="!isPublic"
      >
        <svg
          xmlns="http://www.w3.org/2000/svg"
          xmlns:xlink="http://www.w3.org/1999/xlink"
          viewBox="0 0 20 20"
          width="20"
          height="20"
        >
          <g fill="none">
            <path
              d="M10 2a3 3 0 0 1 3 3v1h1a2 2 0 0 1 2 2v7a2 2 0 0 1-2 2H6a2 2 0 0 1-2-2V8a2 2 0 0 1 2-2h1V5a3 3 0 0 1 3-3zm4 5H6a1 1 0 0 0-1 1v7a1 1 0 0 0 1 1h8a1 1 0 0 0 1-1V8a1 1 0 0 0-1-1zm-4 3.5a1 1 0 1 1 0 2a1 1 0 0 1 0-2zM10 3a2 2 0 0 0-2 2v1h4V5a2 2 0 0 0-2-2z"
              fill="currentColor"
            ></path>
          </g></svg
        >私密>
      </div>
      <div class="flex justify-between mt-4 mr-8 gap-4 mx-6">
        <button
          class="text-gray-800 px-4 py-2 rounded-md"
          @click="emits('cancel')"
        >
          取消
        </button>
        <button class="text-orange-400 px-4 py-2 rounded-md" @click="post">
          发表
        </button>
      </div>
    </div>
  </div>
</template>

<script setup>
import { defineProps, defineEmits, ref, onMounted } from "vue";
import Rate from "@/components/Rate.vue";
import { postComment, updateComment } from "@/api/book-api.js";
import { useToast } from "vue-toast-notification";

const toast = useToast();
const emits = defineEmits(["cancel", "refresh"]);
const props = defineProps(["bookInfo", "comment"]);
const score = ref(0);
const comment = ref("");
const isPublic = ref(true);
const recommendation = ref(0); // 0: 无推荐，1: 推荐，2: 一般，3: 不行

onMounted(() => {
  if (props.comment) {
    score.value = props.comment.score;
    comment.value = props.comment.comment;
    recommendation.value = props.comment.recommendation;
    isPublic.value = props.comment.isPublic;
  }
});

const update = (newScore) => {
  score.value = newScore;
};

const setRecommendation = (rec) => {
  recommendation.value = rec;
};

const post = () => {
  const data = {
    bookId: props.bookInfo.id,
    score: score.value,
    comment: comment.value,
    isPublic: isPublic.value ? 1 : 0,
    recommendation: recommendation.value,
  };
  console.log("提交评论：", data);
  if (props.comment) {
    data.id = props.comment.id;
    updateComment(data)
      .then((response) => {
        toast.success("更新成功");
        emits("cancel");
        emits("refresh");
      })
      .catch((error) => {
        toast.error("更新失败");
      });
    return;
  }
  postComment(data)
    .then((response) => {
      toast.success("发表成功");
      emits("cancel");
      emits("refresh");
    })
    .catch((error) => {
      toast.error("发表失败");
    });
};
</script>

<style>
</style>