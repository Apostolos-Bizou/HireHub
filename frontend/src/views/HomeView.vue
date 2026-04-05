<script setup>
import { ref, onMounted } from "vue"
import { useAuthStore } from "@/stores/auth"
import api from "@/services/api"

const auth = useAuthStore()
const posts = ref([])
const newPost = ref("")

onMounted(async () => {
  try {
    const { data } = await api.get("/feed", { params: { page: 0, size: 20 } })
    posts.value = data.content || []
  } catch { /* demo mode */ }
})

const initials = (name) => name ? name.split(" ").map(w => w[0]).join("").substring(0, 2).toUpperCase() : "?"

async function createPost() {
  if (!newPost.value.trim()) return
  try {
    await api.post("/feed", { content: newPost.value })
    newPost.value = ""
  } catch {}
}

const trending = [
  { topic: "ECDIS 2026 updates", count: 266 },
  { topic: "Red Sea routing changes", count: 525 },
  { topic: "Decarbonization IMO 2030", count: 131 },
  { topic: "Seafarer mental health", count: 477 },
  { topic: "STCW amendments 2026", count: 374 }
]

const openings = [
  { title: "2nd Officer — LPG", company: "Dorian LPG", time: "2d ago" },
  { title: "Chief Engineer — Bulk", company: "Diana Shipping", time: "3d ago" },
  { title: "Master — Container", company: "Costamare", time: "5d ago" },
  { title: "3rd Engineer — Tanker", company: "Tsakos Energy", time: "1w ago" }
]
</script>

<template>
  <div class="container feed-layout">
    <!-- Left Sidebar -->
    <aside class="feed-sidebar-left">
      <div class="card profile-mini">
        <div class="profile-banner"></div>
        <div class="avatar avatar-lg avatar-owner" style="margin:-28px auto 0">
          {{ initials(auth.user?.fullName) }}
        </div>
        <h3>{{ auth.user?.fullName }}</h3>
        <p class="role-label">{{ auth.user?.role?.replace('_', ' ') }}</p>
        <div class="mini-stats">
          <div><strong>12</strong><span>Vessels</span></div>
          <div><strong>240</strong><span>Crew</span></div>
        </div>
      </div>
      <nav class="sidebar-nav card">
        <router-link to="/" class="active">Home</router-link>
        <router-link to="/search">Talent Search</router-link>
        <router-link to="/shortlists">Shortlists</router-link>
        <router-link to="/messages">Messages</router-link>
        <router-link to="/notifications">Notifications</router-link>
        <router-link to="/settings">Settings</router-link>
      </nav>
    </aside>

    <!-- Main Feed -->
    <main class="feed-main">
      <div class="card post-create">
        <div class="post-create-row">
          <div class="avatar avatar-md avatar-owner">{{ initials(auth.user?.fullName) }}</div>
          <input class="input" v-model="newPost" placeholder="Start a post..." @keyup.enter="createPost" />
        </div>
        <div class="post-actions">
          <button class="post-action">📷 Photo</button>
          <button class="post-action">🎬 Video</button>
          <button class="post-action">📄 Article</button>
          <button class="post-action">📜 Certificate</button>
        </div>
      </div>

      <div v-for="post in posts" :key="post.id" class="card post-card">
        <div class="post-header">
          <div class="avatar avatar-md avatar-seafarer">{{ initials(post.authorName || 'U') }}</div>
          <div>
            <strong>{{ post.authorName || 'User' }}</strong>
            <p class="post-meta">{{ post.postType }} · {{ post.createdAt }}</p>
          </div>
        </div>
        <p class="post-body">{{ post.content }}</p>
        <div class="post-engagement">
          <span>👍 {{ post.likesCount }} · {{ post.commentsCount }} comments</span>
        </div>
        <div class="post-footer">
          <button>👍 Like</button>
          <button>💬 Comment</button>
          <button>↗ Share</button>
        </div>
      </div>

      <!-- Demo posts if no API -->
      <div v-if="!posts.length" class="card post-card">
        <div class="post-header">
          <div class="avatar avatar-md avatar-seafarer">MS</div>
          <div><strong>Capt. Maria Santos</strong><p class="post-meta">Master Mariner · 25 yrs · 2h ago</p></div>
        </div>
        <p class="post-body">Proud to announce 25 years of sea service. From cadet to captain — never stop learning. Thank you to all my mentors and crew mates along the way. Fair winds and following seas to all! 🚢</p>
        <div class="post-engagement"><span>👍😊 234 · 45 comments</span></div>
        <div class="post-footer"><button>👍 Like</button><button>💬 Comment</button><button>↗ Share</button></div>
      </div>
      <div v-if="!posts.length" class="card post-card">
        <div class="post-header">
          <div class="avatar avatar-md avatar-agent">ST</div>
          <div><strong>STCW Training Center Manila</strong><p class="post-meta">Maritime Training Provider · 5h ago</p></div>
        </div>
        <p class="post-body">New ECDIS 2026 update training now available! All officers must complete the refresher course before December 2026. Enroll now at our Intramuros campus. Early bird discount for HireHub members.</p>
        <div class="post-engagement"><span>👍😊 89 · 12 comments</span></div>
        <div class="post-footer"><button>👍 Like</button><button>💬 Comment</button><button>↗ Share</button></div>
      </div>
    </main>

    <!-- Right Sidebar -->
    <aside class="feed-sidebar-right">
      <div class="card sidebar-section">
        <h3>Trending in maritime</h3>
        <div v-for="t in trending" :key="t.topic" class="trending-item">
          <strong>{{ t.topic }}</strong>
          <span>{{ t.count }} posts</span>
        </div>
      </div>
      <div class="card sidebar-section">
        <h3>Latest openings</h3>
        <div v-for="j in openings" :key="j.title" class="job-item">
          <a href="#">{{ j.title }}</a>
          <span>{{ j.company }} · {{ j.time }}</span>
        </div>
      </div>
    </aside>
  </div>
</template>

<style scoped>
.feed-layout { display: grid; grid-template-columns: 220px 1fr 280px; gap: var(--space-5); padding-top: var(--space-5); padding-bottom: var(--space-8); }
.profile-banner { height: 56px; background: linear-gradient(135deg, var(--color-primary), var(--color-primary-dark)); border-radius: var(--radius-lg) var(--radius-lg) 0 0; }
.profile-mini { text-align: center; padding-bottom: var(--space-4); }
.profile-mini h3 { font: var(--font-h3); margin-top: var(--space-2); }
.role-label { font: var(--font-caption); color: var(--color-text-secondary); text-transform: capitalize; }
.mini-stats { display: flex; justify-content: center; gap: var(--space-5); margin-top: var(--space-3); }
.mini-stats div { text-align: center; }
.mini-stats strong { display: block; font: var(--font-h3); color: var(--color-primary); }
.mini-stats span { font: var(--font-caption); color: var(--color-text-secondary); }
.sidebar-nav { padding: var(--space-2) 0; margin-top: var(--space-3); }
.sidebar-nav a { display: block; padding: var(--space-2) var(--space-4); font: var(--font-body); color: var(--color-text-secondary); text-decoration: none; }
.sidebar-nav a:hover { background: var(--color-surface); }
.sidebar-nav a.active { color: var(--color-primary); font-weight: 500; border-left: 3px solid var(--color-primary); }
.post-create { padding: var(--space-4); margin-bottom: var(--space-3); }
.post-create-row { display: flex; gap: var(--space-3); align-items: center; }
.post-create-row .input { border-radius: 20px; }
.post-actions { display: flex; gap: var(--space-2); margin-top: var(--space-3); justify-content: center; }
.post-action { background: none; border: none; font: var(--font-small); color: var(--color-text-secondary); cursor: pointer; padding: var(--space-1) var(--space-2); border-radius: var(--radius-sm); }
.post-action:hover { background: var(--color-surface); }
.post-card { padding: var(--space-4); margin-bottom: var(--space-3); }
.post-header { display: flex; gap: var(--space-3); margin-bottom: var(--space-3); }
.post-header strong { font: var(--font-body); font-weight: 500; }
.post-meta { font: var(--font-caption); color: var(--color-text-tertiary); }
.post-body { font: var(--font-body); line-height: 1.6; margin-bottom: var(--space-3); }
.post-engagement { font: var(--font-caption); color: var(--color-text-secondary); padding-bottom: var(--space-3); border-bottom: 1px solid var(--color-border); }
.post-footer { display: flex; justify-content: space-around; padding-top: var(--space-2); }
.post-footer button { background: none; border: none; font: var(--font-small); color: var(--color-text-secondary); cursor: pointer; padding: var(--space-2); border-radius: var(--radius-sm); }
.post-footer button:hover { background: var(--color-surface); }
.sidebar-section { padding: var(--space-4); margin-bottom: var(--space-3); }
.sidebar-section h3 { font: var(--font-h3); margin-bottom: var(--space-3); }
.trending-item { margin-bottom: var(--space-3); }
.trending-item strong { display: block; font: var(--font-small); font-weight: 500; }
.trending-item span { font: var(--font-caption); color: var(--color-text-tertiary); }
.job-item { margin-bottom: var(--space-3); }
.job-item a { display: block; font: var(--font-small); font-weight: 500; }
.job-item span { font: var(--font-caption); color: var(--color-text-tertiary); }

@media (max-width: 1024px) { .feed-layout { grid-template-columns: 1fr; } .feed-sidebar-left, .feed-sidebar-right { display: none; } }
</style>
