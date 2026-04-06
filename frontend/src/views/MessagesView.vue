<script setup>
import { ref, computed, nextTick, watch } from "vue"
import { useAuthStore } from "@/stores/auth"

const auth = useAuthStore()
const photoMap = { 'simos.varias@email.com': '/profiles/simos-varias.jpg' }
const userPhoto = computed(() => photoMap[auth.user?.email] || null)

const threads = ref([
  { id: "1", name: "Juan Dela Cruz", role: "seafarer", subtitle: "3rd Officer · MT Pacific Voyager", online: true, unread: true,
    context: "Juan is on your shortlist for MT Pacific Voyager (3rd Officer)" },
  { id: "2", name: "Magsaysay Maritime", role: "agent", subtitle: "Manning Agent · DMW License #2024-0001", online: true, unread: true,
    context: "Shortlist forwarded: 3rd Officer — MT Pacific Voyager" },
  { id: "3", name: "Ruslan Goncharov", role: "seafarer", subtitle: "2nd Officer · Ukrainian", online: false, unread: false,
    context: null },
  { id: "4", name: "Diana Shipping HR", role: "owner", subtitle: "Fleet Manager · Athens", online: true, unread: true,
    context: "Interview scheduled for Chief Officer position" },
  { id: "5", name: "Capt. Georgios Papandreou", role: "seafarer", subtitle: "Fleet Manager · Tsakos Energy", online: false, unread: false,
    context: null }
])

const allMessages = ref({
  "1": [
    { id: "1a", sender: "me", body: "Hi Juan, I noticed your profile on HireHub. We have a 3rd Officer position on MT Pacific Voyager. Would you be interested?", time: "3h ago" },
    { id: "1b", sender: "other", body: "Thank you for considering me for this opportunity! I am very interested. My COC and STCW are both valid and I'm available from June 15.", time: "2h ago" },
    { id: "1c", sender: "me", body: "Excellent. I will forward your details to our manning agent, Magsaysay Maritime, for contract processing.", time: "2h ago" },
    { id: "1d", sender: "other", body: "That sounds great! I have all my documents ready including PEME, flag endorsements, and tanker certificates. Should I upload them to my HireHub profile?", time: "1h ago" },
    { id: "1e", sender: "me", body: "Yes please. Upload everything to your Document Vault and share them with us through the platform. Our agent will review them.", time: "45m ago" }
  ],
  "2": [
    { id: "2a", sender: "me", body: "Good morning. I'm forwarding a shortlist of 3 candidates for the 3rd Officer position on MT Pacific Voyager. Please review and proceed with PEME scheduling.", time: "5h ago" },
    { id: "2b", sender: "other", body: "Good morning! We received the shortlist. All three candidates look strong. We'll start with Juan Dela Cruz — his profile is very impressive.", time: "4h ago" },
    { id: "2c", sender: "other", body: "We've initiated contact with Juan. His documents are all valid. We can schedule PEME for next week if approved.", time: "3h ago" },
    { id: "2d", sender: "me", body: "Approved. Please proceed with PEME for Juan Dela Cruz. Target crew change date is July 1.", time: "2h ago" },
    { id: "2e", sender: "other", body: "Understood. PEME booked for June 10 at Manila Doctors Hospital. We'll send the deployment schedule once medical clearance is confirmed.", time: "1h ago" }
  ],
  "3": [
    { id: "3a", sender: "other", body: "Hello, I saw the Chief Officer opening for your fleet. I have 8 years tanker experience and am available immediately.", time: "2d ago" },
    { id: "3b", sender: "me", body: "Hi Ruslan, thank you for reaching out. Could you please update your HireHub profile with your latest sea service records?", time: "1d ago" },
    { id: "3c", sender: "other", body: "I am available from June 15. I have updated my profile with latest records from Sovcomflot and Dynacom.", time: "1d ago" },
    { id: "3d", sender: "me", body: "Thank you. I will review your profile and get back to you within the week.", time: "20h ago" }
  ],
  "4": [
    { id: "4a", sender: "other", body: "Dear Simos, we reviewed your application for the Chief Officer position on our Suezmax fleet. We'd like to schedule a video interview.", time: "6h ago" },
    { id: "4b", sender: "me", body: "Thank you for the opportunity! I'm very interested in the position. When would be convenient for the interview?", time: "5h ago" },
    { id: "4c", sender: "other", body: "Would Thursday at 10:00 AM Greek time work for you? The interview will be with our Fleet Manager and Technical Superintendent.", time: "4h ago" },
    { id: "4d", sender: "me", body: "Thursday 10:00 AM works perfectly. Should I prepare any specific documents or presentations?", time: "3h ago" },
    { id: "4e", sender: "other", body: "Please have your certificates ready for verification. We'll also discuss your experience with cargo operations and vetting inspections. Looking forward to meeting you!", time: "2h ago" },
    { id: "4f", sender: "me", body: "Excellent, I'll have everything prepared. Thank you for the opportunity. See you Thursday!", time: "1h ago" }
  ],
  "5": [
    { id: "5a", sender: "other", body: "Simos, just wanted to let you know that your reference letter is ready. You can download it from the company portal.", time: "3d ago" },
    { id: "5b", sender: "me", body: "Thank you Captain! I really appreciate your support throughout my time at Tsakos.", time: "3d ago" },
    { id: "5c", sender: "other", body: "You've earned it. You're one of the best officers I've worked with. Best of luck with your next position — you'll make a great Master one day.", time: "2d ago" },
    { id: "5d", sender: "me", body: "That means a lot coming from you. Fair winds and following seas, Captain.", time: "2d ago" }
  ]
})

const activeThread = ref("1")
const newMessage = ref("")
const searchQuery = ref("")

const currentThread = computed(() => threads.value.find(t => t.id === activeThread.value))
const currentMessages = computed(() => allMessages.value[activeThread.value] || [])
const lastMessage = (id) => {
  const msgs = allMessages.value[id]
  if (!msgs || !msgs.length) return ""
  return msgs[msgs.length - 1].body.substring(0, 40) + "..."
}
const lastTime = (id) => {
  const msgs = allMessages.value[id]
  if (!msgs || !msgs.length) return ""
  return msgs[msgs.length - 1].time
}

const filteredThreads = computed(() => {
  if (!searchQuery.value) return threads.value
  const q = searchQuery.value.toLowerCase()
  return threads.value.filter(t => t.name.toLowerCase().includes(q) || t.subtitle.toLowerCase().includes(q))
})

function selectThread(id) {
  activeThread.value = id
  const thread = threads.value.find(t => t.id === id)
  if (thread) thread.unread = false
  nextTick(() => {
    const el = document.querySelector('.chat-messages')
    if (el) el.scrollTop = el.scrollHeight
  })
}

function sendMessage() {
  if (!newMessage.value.trim()) return
  const msgs = allMessages.value[activeThread.value]
  msgs.push({
    id: Date.now().toString(),
    sender: "me",
    body: newMessage.value,
    time: "Just now"
  })
  newMessage.value = ""
  nextTick(() => {
    const el = document.querySelector('.chat-messages')
    if (el) el.scrollTop = el.scrollHeight
  })
}

const initials = (name) => name ? name.split(' ').map(w => w[0]).join('').substring(0, 2).toUpperCase() : '?'
const unreadCount = computed(() => threads.value.filter(t => t.unread).length)
</script>

<template>
  <div class="container msg-layout">
    <aside class="card thread-list">
      <div class="thread-header">
        <h3>Messages <span v-if="unreadCount" class="unread-badge">{{ unreadCount }}</span></h3>
      </div>
      <div class="search-box"><input class="input" v-model="searchQuery" placeholder="Search messages..." /></div>
      <div v-for="t in filteredThreads" :key="t.id" class="thread-item" :class="{ active: activeThread === t.id }" @click="selectThread(t.id)">
        <div class="avatar avatar-md" :class="'avatar-' + t.role">{{ initials(t.name) }}</div>
        <div class="thread-info">
          <div class="thread-top">
            <strong :class="{ 'thread-unread': t.unread }">{{ t.name }}</strong>
            <span class="thread-time">{{ lastTime(t.id) }}</span>
          </div>
          <p class="thread-preview">{{ lastMessage(t.id) }}</p>
        </div>
        <div v-if="t.unread" class="unread-dot"></div>
      </div>
    </aside>

    <main class="card chat-panel">
      <div class="chat-header">
        <div class="avatar avatar-md" :class="'avatar-' + currentThread?.role">{{ initials(currentThread?.name) }}</div>
        <div class="chat-header-info">
          <strong>{{ currentThread?.name }}</strong>
          <p>{{ currentThread?.subtitle }} · <span :class="currentThread?.online ? 'status-online' : 'status-offline'">{{ currentThread?.online ? 'Online' : 'Offline' }}</span></p>
        </div>
        <div class="chat-header-actions">
          <button class="btn btn-tertiary btn-sm" title="View profile">Profile</button>
          <button class="btn btn-tertiary btn-sm" title="More options">···</button>
        </div>
      </div>

      <div v-if="currentThread?.context" class="context-banner">
        &#128203; {{ currentThread.context }}
      </div>

      <div class="chat-messages">
        <div class="chat-date-sep">Today</div>
        <div v-for="m in currentMessages" :key="m.id" class="message" :class="m.sender === 'me' ? 'sent' : 'received'">
          <div class="bubble">{{ m.body }}</div>
          <span class="msg-time">{{ m.time }}</span>
        </div>
      </div>

      <div class="chat-compose">
        <button class="compose-btn" title="Attach file">&#128206;</button>
        <input class="input compose-input" v-model="newMessage" placeholder="Write a message..." @keyup.enter="sendMessage" />
        <button class="btn btn-primary send-btn" @click="sendMessage" :disabled="!newMessage.trim()">&#10148;</button>
      </div>
    </main>
  </div>
</template>

<style scoped>
.msg-layout { display: grid; grid-template-columns: 340px 1fr; gap: 0; padding-top: 16px; padding-bottom: 48px; height: calc(100vh - 80px); }
.thread-list { border-radius: 12px 0 0 12px; overflow-y: auto; display: flex; flex-direction: column; }
.thread-header { padding: 16px 16px 8px; display: flex; align-items: center; justify-content: space-between; }
.thread-header h3 { font-size: 16px; font-weight: 600; display: flex; align-items: center; gap: 8px; }
.unread-badge { background: var(--color-primary); color: white; font-size: 11px; padding: 2px 8px; border-radius: 10px; }
.search-box { padding: 0 12px 12px; }
.search-box .input { border-radius: 20px; font-size: 13px; padding: 8px 16px; }
.thread-item { display: flex; align-items: center; gap: 12px; padding: 12px 16px; cursor: pointer; border-left: 3px solid transparent; transition: background 0.15s; }
.thread-item:hover { background: var(--color-surface); }
.thread-item.active { background: var(--color-primary-light); border-left-color: var(--color-primary); }
.thread-info { flex: 1; min-width: 0; }
.thread-top { display: flex; justify-content: space-between; align-items: center; }
.thread-top strong { font-size: 13px; }
.thread-unread { color: var(--color-primary); }
.thread-time { font-size: 11px; color: var(--color-text-tertiary); }
.thread-preview { font-size: 12px; color: var(--color-text-secondary); overflow: hidden; text-overflow: ellipsis; white-space: nowrap; margin-top: 2px; }
.unread-dot { width: 8px; height: 8px; background: var(--color-primary); border-radius: 50%; flex-shrink: 0; }

.chat-panel { border-radius: 0 12px 12px 0; display: flex; flex-direction: column; }
.chat-header { display: flex; align-items: center; gap: 12px; padding: 12px 16px; border-bottom: 1px solid var(--color-border); }
.chat-header-info { flex: 1; }
.chat-header-info strong { font-size: 14px; }
.chat-header-info p { font-size: 11px; color: var(--color-text-secondary); margin-top: 2px; }
.status-online { color: #1B5E20; font-weight: 500; }
.status-offline { color: var(--color-text-tertiary); }
.chat-header-actions { display: flex; gap: 4px; }
.btn-sm { font-size: 12px; padding: 6px 12px; }

.context-banner { margin: 12px 16px 0; padding: 8px 12px; border-radius: 8px; font-size: 13px; background: var(--color-primary-light); color: var(--color-primary-dark); }

.chat-messages { flex: 1; overflow-y: auto; padding: 16px; display: flex; flex-direction: column; gap: 12px; }
.chat-date-sep { text-align: center; font-size: 11px; color: var(--color-text-tertiary); padding: 4px 0 8px; }
.message { max-width: 70%; }
.sent { align-self: flex-end; }
.received { align-self: flex-start; }
.bubble { padding: 10px 14px; border-radius: 16px; font-size: 14px; line-height: 1.5; }
.sent .bubble { background: var(--color-primary); color: white; border-bottom-right-radius: 4px; }
.received .bubble { background: var(--color-surface); border-bottom-left-radius: 4px; }
.msg-time { font-size: 10px; color: var(--color-text-tertiary); margin-top: 4px; display: block; }
.sent .msg-time { text-align: right; }

.chat-compose { display: flex; gap: 8px; padding: 12px 16px; border-top: 1px solid var(--color-border); align-items: center; }
.compose-btn { background: none; border: none; font-size: 20px; cursor: pointer; padding: 4px; color: var(--color-text-secondary); }
.compose-btn:hover { color: var(--color-primary); }
.compose-input { border-radius: 20px; flex: 1; font-size: 13px; padding: 10px 16px; }
.send-btn { border-radius: 50%; width: 38px; height: 38px; padding: 0; font-size: 16px; display: flex; align-items: center; justify-content: center; }
.send-btn:disabled { opacity: 0.5; cursor: not-allowed; }

@media (max-width: 768px) { .msg-layout { grid-template-columns: 1fr; } .thread-list { display: none; } }
</style>
