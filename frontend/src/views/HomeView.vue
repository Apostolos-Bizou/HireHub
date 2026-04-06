<script setup>
import { ref, computed, onMounted } from "vue"
import { useAuthStore } from "@/stores/auth"
import api from "@/services/api"

const auth = useAuthStore()
const photoMap = { 'simos.varias@email.com': '/profiles/simos-varias.jpg' }
const userPhoto = computed(() => photoMap[auth.user?.email] || null)
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

const isSeafarer = computed(() => auth.user?.role === 'SEAFARER')
const isShipowner = computed(() => auth.user?.role === 'SHIPOWNER')
const isAgent = computed(() => auth.user?.role === 'MANNING_AGENT')

const avatarClass = computed(() => isSeafarer.value ? 'avatar-seafarer' : isAgent.value ? 'avatar-agent' : 'avatar-owner')
const bannerClass = computed(() => isShipowner.value ? 'banner-owner' : isAgent.value ? 'banner-agent' : 'banner-seafarer')

const sidebarNav = computed(() => {
  if (isShipowner.value) return [
    { to: '/', label: 'Home' },
    { to: '/search', label: 'Talent Search' },
    { to: '/shortlists', label: 'Shortlists' },
    { to: '/profile', label: 'Company Profile' },
    { to: '/messages', label: 'Messages' },
    { to: '/settings', label: 'Settings' }
  ]
  if (isAgent.value) return [
    { to: '/', label: 'Home' },
    { to: '/agent', label: 'Dashboard' },
    { to: '/messages', label: 'Messages' },
    { to: '/notifications', label: 'Notifications' },
    { to: '/settings', label: 'Settings' }
  ]
  return [
    { to: '/', label: 'Home' },
    { to: '/profile', label: 'My Profile' },
    { to: '/messages', label: 'Messages' },
    { to: '/notifications', label: 'Notifications' },
    { to: '/settings', label: 'Settings' }
  ]
})

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
        <div class="profile-banner" :class="bannerClass"></div>
        <img v-if="userPhoto" :src="userPhoto" class="mini-photo" />
        <div v-else class="avatar avatar-lg" :class="avatarClass" style="margin:-28px auto 0">
          {{ initials(auth.user?.fullName) }}
        </div>
        <h3>{{ auth.user?.fullName }}</h3>
        <p class="role-label">{{ auth.user?.role?.replace('_', ' ') }}</p>
        <div v-if="isSeafarer" class="mini-stats">
          <div><strong>4 yrs</strong><span>Sea Service</span></div>
          <div><strong>347</strong><span>Profile Views</span></div>
          <div><strong>12</strong><span>Shortlists</span></div>
        </div>
        <div v-if="isShipowner" class="mini-stats">
          <div><strong>65</strong><span>Vessels</span></div>
          <div><strong>1,560</strong><span>Active Crew</span></div>
          <div><strong>3</strong><span>Open Needs</span></div>
        </div>
        <div v-if="isAgent" class="mini-stats">
          <div><strong>8</strong><span>Principals</span></div>
          <div><strong>487</strong><span>Deployments</span></div>
          <div><strong>94%</strong><span>Placement</span></div>
        </div>
      </div>
      <nav class="sidebar-nav card">
        <router-link v-for="item in sidebarNav" :key="item.to" :to="item.to" :class="{ active: item.to === '/' }">{{ item.label }}</router-link>
      </nav>
    </aside>

    <!-- Main Feed -->
    <main class="feed-main">
      <div v-if="isShipowner" class="card quick-banner">
        <div class="quick-banner-inner">
          <div class="quick-banner-text">
            <strong>Welcome back, {{ auth.user?.fullName?.split(' ')[0] }}</strong>
            <span>You have 3 urgent crew needs and 2 new shortlists pending review.</span>
          </div>
          <div class="quick-banner-actions">
            <router-link to="/search" class="btn btn-primary btn-sm">Search Talent</router-link>
            <router-link to="/shortlists" class="btn btn-secondary btn-sm">View Shortlists</router-link>
          </div>
        </div>
      </div>
      <div v-if="isAgent" class="card quick-banner quick-banner-agent">
        <div class="quick-banner-inner">
          <div class="quick-banner-text">
            <strong>Welcome back, {{ auth.user?.fullName?.split(' ')[0] }}</strong>
            <span>You have 3 new shortlist requests from shipowners.</span>
          </div>
          <div class="quick-banner-actions">
            <router-link to="/agent" class="btn btn-primary btn-sm">View Requests</router-link>
          </div>
        </div>
      </div>

      <div class="card post-create">
        <div class="post-create-row">
          <img v-if="userPhoto" :src="userPhoto" style="width:40px;height:40px;border-radius:50%;object-fit:cover;" />
          <div v-else class="avatar avatar-md" :class="avatarClass">{{ initials(auth.user?.fullName) }}</div>
          <input class="input" v-model="newPost" placeholder="Start a post..." @keyup.enter="createPost" />
        </div>
        <div class="post-actions">
          <button class="post-action">📷 Photo</button>
          <button class="post-action">🎬 Video</button>
          <button class="post-action">📄 Article</button>
          <button v-if="isSeafarer" class="post-action">📜 Certificate</button>
        </div>
      </div>

      <div v-for="post in posts" :key="post.id" class="card post-card">
        <div class="post-header">
          <div class="avatar avatar-md avatar-seafarer">{{ initials(post.authorName || 'U') }}</div>
          <div><strong>{{ post.authorName || 'User' }}</strong><p class="post-meta">{{ post.postType }} · {{ post.createdAt }}</p></div>
        </div>
        <p class="post-body">{{ post.content }}</p>
        <div class="post-engagement"><span>👍 {{ post.likesCount }} · {{ post.commentsCount }} comments</span></div>
        <div class="post-footer"><button>👍 Like</button><button>💬 Comment</button><button>↗ Share</button></div>
      </div>

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
      <div v-if="!posts.length && isShipowner" class="card post-card">
        <div class="post-header">
          <div class="avatar avatar-md avatar-owner">IM</div>
          <div><strong>InterManager</strong><p class="post-meta">Industry Association · 1d ago</p></div>
        </div>
        <p class="post-body">New crew retention study released: Companies investing in career development platforms see 28% lower turnover rates. Digital visibility tools for seafarers are becoming a key competitive advantage in crew management.</p>
        <div class="post-engagement"><span>👍 156 · 32 comments</span></div>
        <div class="post-footer"><button>👍 Like</button><button>💬 Comment</button><button>↗ Share</button></div>
      </div>
    </main>

    <!-- Right Sidebar -->
    <aside class="feed-sidebar-right">
      <a href="https://healthnewsnext2me.dn2me.com/en/category/shipcare" target="_blank" class="card shipcare-card">
        <img src="https://healthnewsnext2me.dn2me.com/sites/default/files/field/image/5th_safety4sea_limassol_forum.jpeg" class="shipcare-img" alt="ShipCare News" />
        <div class="shipcare-body">
          <span class="shipcare-tag">ShipCare News</span>
          <h3>Philippines DMW issues call to protect seafarers from warlike areas</h3>
          <p class="shipcare-excerpt">The Department of Migrant Workers has urged manning agencies and seafarers to exercise caution regarding deployment to...</p>
          <span class="shipcare-date">14 Jul 2025 · HealthNews Next2Me</span>
        </div>
      </a>
      <div class="card sidebar-section">
        <h3>Trending in maritime</h3>
        <div v-for="t in trending" :key="t.topic" class="trending-item">
          <strong>{{ t.topic }}</strong>
          <span>{{ t.count }} posts</span>
        </div>
      </div>
      <div v-if="isSeafarer" class="card sidebar-section">
        <h3>Latest openings</h3>
        <div v-for="j in openings" :key="j.title" class="job-item">
          <a href="#">{{ j.title }}</a>
          <span>{{ j.company }} · {{ j.time }}</span>
        </div>
      </div>
      <div v-if="isShipowner" class="card sidebar-section">
        <h3>Market insights</h3>
        <div class="insight-item"><strong>Filipino officers</strong><span>High availability · Avg 3-week notice</span></div>
        <div class="insight-item"><strong>Tanker specialists</strong><span>Moderate demand · +5% YoY</span></div>
        <div class="insight-item"><strong>ECDIS certified</strong><span>Strong demand · 2,400+ on HireHub</span></div>
      </div>
      <div v-if="isAgent" class="card sidebar-section">
        <h3>Pipeline summary</h3>
        <div class="insight-item"><strong>Received</strong><span>3 candidates</span></div>
        <div class="insight-item"><strong>Vetting</strong><span>5 candidates</span></div>
        <div class="insight-item"><strong>Contract</strong><span>2 candidates</span></div>
        <div class="insight-item"><strong>Deployment</strong><span>2 candidates</span></div>
      </div>
    </aside>
  </div>
</template>

<style scoped>
.feed-layout { display: grid; grid-template-columns: 200px 1fr 280px; gap: var(--space-5); padding-top: var(--space-5); padding-bottom: var(--space-8); }
.profile-banner { height: 56px; border-radius: var(--radius-lg) var(--radius-lg) 0 0; }
.banner-seafarer { background: linear-gradient(135deg, var(--color-primary), var(--color-primary-dark)); }
.banner-owner { background: linear-gradient(135deg, var(--color-accent), #D4912F); }
.banner-agent { background: linear-gradient(135deg, var(--color-agent), #157A5C); }
.profile-mini { text-align: center; padding-bottom: var(--space-4); }
.profile-mini h3 { font: var(--font-h3); margin-top: var(--space-2); }
.mini-photo { width: 56px; height: 56px; border-radius: 50%; object-fit: cover; border: 3px solid white; margin: -28px auto 0; display: block; }
.role-label { font: var(--font-caption); color: var(--color-text-secondary); text-transform: capitalize; }
.mini-stats { display: flex; justify-content: center; gap: var(--space-3); margin-top: var(--space-2); }
.mini-stats div { text-align: center; line-height: 1.3; }
.mini-stats strong { display: block; font-size: 18px; font-weight: 600; color: var(--color-primary); }
.mini-stats span { font-size: 10px; color: var(--color-text-tertiary); line-height: 1.2; }
.sidebar-nav { padding: var(--space-2) 0; margin-top: var(--space-3); }
.sidebar-nav a { display: block; padding: var(--space-2) var(--space-4); font: var(--font-body); color: var(--color-text-secondary); text-decoration: none; }
.sidebar-nav a:hover { background: var(--color-surface); text-decoration: none; }
.sidebar-nav a.active { color: var(--color-primary); font-weight: 500; border-left: 3px solid var(--color-primary); }
.quick-banner { padding: var(--space-4); margin-bottom: var(--space-3); border-left: 4px solid var(--color-primary); }
.quick-banner-agent { border-left-color: var(--color-agent); }
.quick-banner-inner { display: flex; justify-content: space-between; align-items: center; gap: var(--space-3); }
.quick-banner-text strong { display: block; font: var(--font-body); font-weight: 600; }
.quick-banner-text span { font: var(--font-small); color: var(--color-text-secondary); }
.quick-banner-actions { display: flex; gap: var(--space-2); flex-shrink: 0; }
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
.insight-item { margin-bottom: var(--space-3); }
.insight-item strong { display: block; font: var(--font-small); font-weight: 500; }
.insight-item span { font: var(--font-caption); color: var(--color-text-tertiary); }
.shipcare-card { display: block; text-decoration: none; color: inherit; overflow: hidden; margin-bottom: var(--space-3); transition: transform 0.2s, box-shadow 0.2s; cursor: pointer; }
.shipcare-card:hover { transform: translateY(-2px); box-shadow: 0 4px 16px rgba(0,0,0,0.12); text-decoration: none; }
.shipcare-img { width: 100%; height: 160px; object-fit: cover; display: block; }
.shipcare-body { padding: 14px; }
.shipcare-tag { display: inline-block; background: #004182; color: white; font-size: 10px; font-weight: 600; padding: 3px 8px; border-radius: 4px; text-transform: uppercase; letter-spacing: 0.5px; margin-bottom: 8px; }
.shipcare-body h3 { font-size: 14px; font-weight: 600; line-height: 1.4; margin-bottom: 6px; color: var(--color-text-primary); }
.shipcare-excerpt { font-size: 12px; color: var(--color-text-secondary); line-height: 1.5; margin-bottom: 8px; display: -webkit-box; -webkit-line-clamp: 3; -webkit-box-orient: vertical; overflow: hidden; }
.shipcare-date { font-size: 10px; color: var(--color-text-tertiary); }
.btn-sm { font-size: 12px; padding: 6px 14px; }
@media (max-width: 1024px) { .feed-layout { grid-template-columns: 1fr; } .feed-sidebar-left, .feed-sidebar-right { display: none; } }
</style>
