<template>
  <div
    class="fixed top-0 left-0 bottom-0 right-0 z-5"
    v-show="utilStore.mask && utilStore.specifyMask === 'none'"
    @click="utilStore.hideMask()"
  ></div>
</template>

<script setup>
import { useUtilStore } from "@/stores/util-store";
import { onMounted } from "vue";
const utilStore = useUtilStore();

onMounted(() => {
  utilStore.hideMask();
});
</script>

<style>

</style>
