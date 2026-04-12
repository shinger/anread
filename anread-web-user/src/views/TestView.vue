<template>
  <div>
    1111
    <li v-for="(item, index) in list" :key="index">
      <p>{{ item }}</p>
      <button @click="list[index] = 'new' + item">change</button>
    </li>
    <button @click="add()">add</button>
  </div>
  
</template>

<script setup>
import { ref } from 'vue'

const list = ref(['aaa', 'bbb', 'ccc'])

const add = () => {
  // 新增一个元素
  let item = 'new item'
  list.value.push(item)
  // 新增一个元素后，将新增的元素设置为 true new item
  item = 'true new item'
}
</script>

<style scoped lang="less">

</style>