<template>
  <main
    class="px-4 sm:px-12 mt-2 flex flex-col items-start md:px-24 lg:px-54"
    v-if="comment"
  >
    <section class="w-full my-1 flex pb-1">
      <div class="w-full">
        <div class="flex items-center justify-between">
          <div class="flex items-center w-full">
            <Avatar :src="comment.avatar" :size="28" />
            <span class="ml-2">{{ comment.username }}</span>
            <span class="text-[12px] text-[#76787c] ml-2">{{
              recomendMap[comment.recommendation]
            }}</span>
          </div>

          <button
            v-if="!comment.isSelf"
            class="w-24 h-6 rounded-2xl bg-gray-300 text-[12px] text-[#595a5b]"
          >
            +关注
          </button>
        </div>
        <div class="w-full text-lg mt-4 min-h-12">
          {{ comment.comment }}
        </div>
        <div class="flex" v-if="comment.isSelf">
          <button
            class="mx-2 text-[#eb9224] text-[12px]"
            @click="showCommentEditor = true"
          >
            编辑
          </button>
          <button
            class="mx-2 text-[#eb9224] text-[12px]"
            @click="onDeleteComment"
          >
            删除
          </button>
        </div>
      </div>
    </section>
    <section class="w-full mt-2">
      <a :href="'/book/' + comment.bookId">
        <div class="w-full flex items-center border-y border-gray-300 pb-4">
          <img class="w-12 h-18 my-1" :src="comment.bookCover" />
          <div class="ml-4 flex flex-col">
            <p class="text-lg font-bold">{{ comment.bookTitle }}</p>
            <p class="text-[12px] text-[#76787c]">{{ comment.bookAuthor }}</p>
          </div>
        </div>
        <div class="text-[12px] text-[#76787c] border-b border-gray-300 py-2">
          发表时间：{{ comment.updateTime }}
        </div>
      </a>
    </section>
    <section class="w-full">
      <div class="flex mt-2">
        <span
          class="mx-2"
          :class="{ 'border-b-2 border-[#eb9224]': onTab == 'comment' }"
          @click="onTab = 'comment'"
          >评论</span
        >
        <span
          class="mx-2"
          :class="{ 'border-b-2 border-[#eb9224]': onTab == 'like' }"
          @click="onTab = 'like'"
          >点赞</span
        >
      </div>
      <div class="mt-2 w-full">
        <div v-if="onTab == 'comment'">
          <li
            v-for="item in comment.subComments"
            :key="item.id"
            class="w-full border-b border-gray-300 py-2"
          >
            <div class="flex items-start ml-2">
              <Avatar class="mt-4" :src="item.avatar" :size="28" />

              <div class="flex flex-col ml-4">
                <div class="flex items-center">
                  <span class="mx-2 text-[#595a5b]">{{ item.username }}</span>
                  <span class="text-[12px] text-[#76787c]">{{
                    item.createTime
                  }}</span>
                </div>
                <div class="w-full text-lg mt-2 min-h-8 ml-2">
                  {{ item.content }}
                </div>
              </div>
            </div>
          </li>
        </div>
        <div v-if="onTab == 'like'">
          <li
            v-for="item in comment.commentLikes"
            :key="item.id"
            class="w-full"
          >
            <div class="flex items-center">
              <Avatar class="mt-2" :src="item.avatar" :size="28" />

              <span class="mx-2 mt-2 text-[#595a5b]">{{ item.username }}</span>
            </div>
          </li>
        </div>
      </div>
    </section>
    <section class="fixed bottom-0">
      <div class="w-full h-16 flex flex-row items-center justify-between">
        <div class="w-64 h-8 bg-gray-200 dark:bg-stone-800 rounded-4xl">
          <input
            class="w-full h-full rounded-4xl px-4"
            type="text"
            placeholder="发表评论..."
            @keyup.enter="addSubComment"
            v-model="subCommentText"
          />
        </div>
        <div class="flex justify-between ml-4">
          <button class="ml-2"><CommentIcon /></button>
          <button class="ml-2" @click="likeComment">
            <LikeIcon :isLiked="comment.hasLiked" />
          </button>
        </div>
      </div>
    </section>
    <CommentEditor
      v-if="showCommentEditor"
      :comment="comment"
      :bookInfo="{ title: comment.bookTitle, id: comment.bookId }"
      @cancel="showCommentEditor = false"
      @refresh="refreshComments"
    />
  </main>
</template>

<script setup>
import { ref, reactive, onMounted, onBeforeMount } from "vue";
import { useRoute, useRouter } from "vue-router";
import {
  getComment,
  postLikeComment,
  postSubComment,
  deleteComment,
} from "@/api/book-api";
import Avatar from "@/components/Avatar.vue";
import CommentIcon from "@/components/icons/CommentIcon.vue";
import CommentEditor from "@/components/CommentEditor.vue";
import LikeIcon from "@/components/icons/LikeIcon.vue";
import MoreIcon from "@/components/icons/MoreIcon.vue";
import { useUserStore } from "@/stores/user.js";

const route = useRoute();
const router = useRouter();
const userStore = useUserStore();

const showCommentEditor = ref(false);
const subCommentText = ref("");
const onTab = ref("comment");
const comment = ref(null);
const recomendMap = {
  0: "",
  1: "推荐",
  2: "一般",
  3: "不行",
};

const refreshComments = () => {
  getComment(route.params.id).then((res) => {
    console.log(res);
    comment.value = res;
  });
};

onMounted(() => {
  refreshComments();
});

const likeComment = () => {
  console.log("like comment");
  postLikeComment(route.params.id).then((res) => {
    refreshComments();
  });
};

const addSubComment = () => {
  postSubComment({
    commentId: route.params.id,
    content: subCommentText.value,
  }).then((res) => {
    refreshComments();
  });
  subCommentText.value = "";
};

const onDeleteComment = () => {
  deleteComment(route.params.id).then((res) => {
    refreshComments();
  });
};
</script>

<style>
</style>