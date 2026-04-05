<script setup>
import { ref } from "vue"
const threads = [
  { id: "1", name: "Juan Dela Cruz", preview: "Thank you for considering me...", time: "2h ago", unread: true, role: "seafarer" },
  { id: "2", name: "Magsaysay Maritime", preview: "We received the shortlist and...", time: "5h ago", unread: false, role: "agent" },
  { id: "3", name: "Ruslan Goncharov", preview: "I am available from June 15...", time: "1d ago", unread: false, role: "seafarer" }
]
const activeThread = ref("1")
const newMessage = ref("")
const messages = ref([
  { id: "1", sender: "me", body: "Hi Juan, I noticed your profile on HireHub. We have a 3rd Officer position on MT Pacific Voyager. Would you be interested?", time: "3h ago" },
  { id: "2", sender: "other", body: "Thank you for considering me for this opportunity! I am very interested. My COC and STCW are both valid and I'm available from June 15.", time: "2h ago" },
  { id: "3", sender: "me", body: "Excellent. I will forward your details to our manning agent, Magsaysay Maritime, for contract processing.", time: "2h ago" }
])
</script>
<template>
  <div class="container msg-layout">
    <aside class="card thread-list">
      <div style="padding:var(--space-3)"><input class="input" placeholder="Search messages..." /></div>
      <div v-for="t in threads" :key="t.id" class="thread-item" :class="{active:activeThread===t.id}" @click="activeThread=t.id">
        <div class="avatar avatar-md" :class="'avatar-'+t.role">{{ t.name.split(' ').map(w=>w[0]).join('').substring(0,2) }}</div>
        <div style="flex:1;min-width:0">
          <div style="display:flex;justify-content:space-between"><strong>{{ t.name }}</strong><span style="font:var(--font-caption);color:var(--color-text-tertiary)">{{ t.time }}</span></div>
          <p style="font:var(--font-small);color:var(--color-text-secondary);overflow:hidden;text-overflow:ellipsis;white-space:nowrap">{{ t.preview }}</p>
        </div>
        <div v-if="t.unread" style="width:8px;height:8px;background:var(--color-primary);border-radius:50%"></div>
      </div>
    </aside>
    <main class="card chat-panel">
      <div class="chat-header">
        <div class="avatar avatar-md avatar-seafarer">JD</div>
        <div><strong>Juan Dela Cruz</strong><p style="font:var(--font-caption);color:var(--color-text-secondary)">3rd Officer · Online</p></div>
      </div>
      <div class="context-banner badge-info">📋 Juan is on your shortlist for MT Pacific Voyager (3rd Officer)</div>
      <div class="chat-messages">
        <div v-for="m in messages" :key="m.id" class="message" :class="m.sender==='me'?'sent':'received'">
          <div class="bubble">{{ m.body }}</div>
          <span class="msg-time">{{ m.time }}</span>
        </div>
      </div>
      <div class="chat-compose">
        <input class="input" v-model="newMessage" placeholder="Write a message..." style="border-radius:20px" />
        <button class="btn btn-primary" style="border-radius:50%;width:36px;height:36px;padding:0">→</button>
      </div>
    </main>
  </div>
</template>
<style scoped>
.msg-layout { display: grid; grid-template-columns: 320px 1fr; gap: 0; padding-top: var(--space-5); padding-bottom: var(--space-8); height: calc(100vh - 80px); }
.thread-list { border-radius: var(--radius-lg) 0 0 var(--radius-lg); overflow-y: auto; }
.thread-item { display: flex; align-items: center; gap: var(--space-3); padding: var(--space-3) var(--space-4); cursor: pointer; border-left: 3px solid transparent; }
.thread-item:hover { background: var(--color-surface); }
.thread-item.active { background: var(--color-primary-light); border-left-color: var(--color-primary); }
.chat-panel { border-radius: 0 var(--radius-lg) var(--radius-lg) 0; display: flex; flex-direction: column; }
.chat-header { display: flex; align-items: center; gap: var(--space-3); padding: var(--space-3) var(--space-4); border-bottom: 1px solid var(--color-border); }
.context-banner { margin: var(--space-3) var(--space-4) 0; padding: var(--space-2) var(--space-3); border-radius: var(--radius-md); font: var(--font-small); }
.chat-messages { flex: 1; overflow-y: auto; padding: var(--space-4); display: flex; flex-direction: column; gap: var(--space-3); }
.message { max-width: 70%; }
.sent { align-self: flex-end; }
.received { align-self: flex-start; }
.bubble { padding: var(--space-3); border-radius: var(--radius-lg); font: var(--font-body); line-height: 1.5; }
.sent .bubble { background: var(--color-primary); color: var(--color-white); }
.received .bubble { background: var(--color-surface); }
.msg-time { font: var(--font-caption); color: var(--color-text-tertiary); margin-top: 2px; display: block; }
.sent .msg-time { text-align: right; }
.chat-compose { display: flex; gap: var(--space-2); padding: var(--space-3) var(--space-4); border-top: 1px solid var(--color-border); }
@media (max-width: 768px) { .msg-layout { grid-template-columns: 1fr; } .thread-list { display: none; } }
</style>
