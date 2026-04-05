<script setup>
const notifications = [
  { icon: "👁", title: "Tsakos Energy viewed your shortlist", time: "1h ago", read: false },
  { icon: "📋", title: "Juan Dela Cruz was added to a shortlist", time: "3h ago", read: false },
  { icon: "💬", title: "New message from Magsaysay Maritime", time: "5h ago", read: true },
  { icon: "📜", title: "Certificate expiring: Tanker Familiarization — Juan D.", time: "1d ago", read: true },
  { icon: "🤖", title: "AI found 3 new matches for your search criteria", time: "2d ago", read: true }
]
</script>
<template>
  <div class="container" style="padding-top:var(--space-5);max-width:700px">
    <h1 style="font:var(--font-h1);margin-bottom:var(--space-4)">Notifications</h1>
    <div v-for="n in notifications" :key="n.title" class="card" :style="{padding:'var(--space-3) var(--space-4)',marginBottom:'var(--space-2)',background:n.read?'var(--color-white)':'var(--color-primary-light)',cursor:'pointer'}">
      <div style="display:flex;align-items:center;gap:var(--space-3)">
        <span style="font-size:20px">{{ n.icon }}</span>
        <div style="flex:1"><strong :style="{fontWeight:n.read?400:500}">{{ n.title }}</strong></div>
        <span style="font:var(--font-caption);color:var(--color-text-tertiary)">{{ n.time }}</span>
        <div v-if="!n.read" style="width:8px;height:8px;background:var(--color-primary);border-radius:50%"></div>
      </div>
    </div>
  </div>
</template>
