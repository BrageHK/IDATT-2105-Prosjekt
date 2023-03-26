import { ref, readonly } from 'vue';

const serverIP = ref('http://192.168.86.142:8080');
const categories = ref([
      "electronics",
      "furniture",
      "clothing",
      "books",
      "toys",
      "sports",
      "tools",
      "misc",
  ]);


export function getIp() {
  return {
    serverIP: readonly(serverIP),
  };
}

export function getCategories() {
  return {
    categories: readonly(categories),
  };
}