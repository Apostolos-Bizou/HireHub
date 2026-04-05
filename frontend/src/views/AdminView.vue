<script setup>
import { ref, onMounted } from "vue"
import api from "@/services/api"

const activeTab = ref("dashboard")
const stats = ref(null)
const users = ref([])
const companies = ref([])
const loading = ref(false)
const userFilter = ref("")
const companyFilter = ref("")

onMounted(() => loadDashboard())

async function loadDashboard() {
  loading.value = true
  try {
    const { data } = await api.get("/admin/dashboard")
    stats.value = data
  } catch (e) { console.error("Failed to load dashboard", e) }
  finally { loading.value = false }
}

async function loadUsers() {
  loading.value = true
  try {
    const params = { page: 0, size: 50 }
    if (userFilter.value) params.role = userFilter.value
    const { data } = await api.get("/admin/users", { params })
    users.value = data.content || []
  } catch (e) { console.error(e) }
  finally { loading.value = false }
}

async function loadCompanies() {
  loading.value = true
  try {
    const params = { page: 0, size: 50 }
    if (companyFilter.value) params.type = companyFilter.value
    const { data } = await api.get("/admin/companies", { params })
    companies.value = data.content || []
  } catch (e) { console.error(e) }
  finally { loading.value = false }
}

async function updateStatus(userId, status) {
  if (!confirm(`Set user status to ${status}?`)) return
  await api.patch(`/admin/users/${userId}/status`, { status })
  loadUsers()
}

async function verifyProfile(profileId) {
  await api.patch(`/admin/profiles/${profileId}/verify`)
  alert("Profile verified")
}

async function verifyDmw(companyId) {
  await api.patch(`/admin/companies/${companyId}/verify-dmw`)
  loadCompanies()
}

function switchTab(tab) {
  activeTab.value = tab
  if (tab === "dashboard") loadDashboard()
  else if (tab === "users") loadUsers()
  else if (tab === "companies") loadCompanies()
}

function fmtDate(d) {
  if (!d) return "—"
  return new Date(d).toLocaleDateString("en-GB", { day: "2-digit", month: "short", year: "numeric" })
}
</script>

<template>
  <div class="container admin-page">
    <div class="admin-header">
      <h1>Admin Panel</h1>
      <p class="subtitle">HireHub Platform Management</p>
    </div>

    <nav class="admin-tabs">
      <button :class="['tab', { active: activeTab === 'dashboard' }]" @click="switchTab('dashboard')">Dashboard</button>
      <button :class="['tab', { active: activeTab === 'users' }]" @click="switchTab('users')">Users</button>
      <button :class="['tab', { active: activeTab === 'companies' }]" @click="switchTab('companies')">Companies</button>
    </nav>

    <div v-if="loading" class="loading-state">Loading...</div>

    <!-- ── Dashboard Tab ─────────────────────────────────── -->
    <section v-if="activeTab === 'dashboard' && stats" class="dashboard-grid">
      <div class="stat-card">
        <div class="stat-value">{{ stats.totalUsers }}</div>
        <div class="stat-label">Total Users</div>
      </div>
      <div class="stat-card seafarer">
        <div class="stat-value">{{ stats.totalSeafarers }}</div>
        <div class="stat-label">Seafarers</div>
      </div>
      <div class="stat-card owner">
        <div class="stat-value">{{ stats.totalShipowners }}</div>
        <div class="stat-label">Shipowners</div>
      </div>
      <div class="stat-card agent">
        <div class="stat-value">{{ stats.totalAgents }}</div>
        <div class="stat-label">Manning Agents</div>
      </div>
      <div class="stat-card success">
        <div class="stat-value">{{ stats.verifiedProfiles }}</div>
        <div class="stat-label">Verified Profiles</div>
      </div>
      <div class="stat-card warning">
        <div class="stat-value">{{ stats.pendingVerifications }}</div>
        <div class="stat-label">Pending Verification</div>
      </div>
      <div class="stat-card">
        <div class="stat-value">{{ stats.activeShortlists }}</div>
        <div class="stat-label">Active Shortlists</div>
      </div>
      <div class="stat-card">
        <div class="stat-value">{{ stats.recentSignups }}</div>
        <div class="stat-label">Signups (7d)</div>
      </div>
    </section>

    <!-- ── Users Tab ─────────────────────────────────────── -->
    <section v-if="activeTab === 'users'">
      <div class="toolbar">
        <select class="select" v-model="userFilter" @change="loadUsers">
          <option value="">All Roles</option>
          <option value="SEAFARER">Seafarer</option>
          <option value="SHIPOWNER">Shipowner</option>
          <option value="MANNING_AGENT">Manning Agent</option>
          <option value="ADMIN">Admin</option>
        </select>
        <span class="result-count">{{ users.length }} users</span>
      </div>
      <div class="card table-card">
        <table class="data-table">
          <thead>
            <tr>
              <th>Email</th>
              <th>Role</th>
              <th>Name / Company</th>
              <th>Status</th>
              <th>Registered</th>
              <th>Actions</th>
            </tr>
          </thead>
          <tbody>
            <tr v-for="u in users" :key="u.id">
              <td>{{ u.email }}</td>
              <td><span class="badge" :class="'badge-' + u.role.toLowerCase()">{{ u.role }}</span></td>
              <td>{{ u.profileName || u.companyName || '—' }}</td>
              <td><span class="badge" :class="'status-' + u.accountStatus.toLowerCase()">{{ u.accountStatus }}</span></td>
              <td>{{ fmtDate(u.createdAt) }}</td>
              <td class="actions-cell">
                <button v-if="u.accountStatus !== 'ACTIVE'" class="btn-xs btn-success" @click="updateStatus(u.id, 'ACTIVE')">Activate</button>
                <button v-if="u.accountStatus !== 'SUSPENDED'" class="btn-xs btn-warning" @click="updateStatus(u.id, 'SUSPENDED')">Suspend</button>
              </td>
            </tr>
          </tbody>
        </table>
      </div>
    </section>

    <!-- ── Companies Tab ─────────────────────────────────── -->
    <section v-if="activeTab === 'companies'">
      <div class="toolbar">
        <select class="select" v-model="companyFilter" @change="loadCompanies">
          <option value="">All Types</option>
          <option value="SHIPOWNER">Shipowner</option>
          <option value="MANNING_AGENT">Manning Agent</option>
          <option value="MANAGER">Manager</option>
        </select>
        <span class="result-count">{{ companies.length }} companies</span>
      </div>
      <div class="card table-card">
        <table class="data-table">
          <thead>
            <tr>
              <th>Company</th>
              <th>Type</th>
              <th>Country</th>
              <th>DMW License</th>
              <th>Status</th>
              <th>Fleet</th>
              <th>Actions</th>
            </tr>
          </thead>
          <tbody>
            <tr v-for="c in companies" :key="c.id">
              <td class="company-name">{{ c.companyName }}</td>
              <td><span class="badge" :class="'badge-' + c.companyType.toLowerCase()">{{ c.companyType }}</span></td>
              <td>{{ c.country || '—' }}</td>
              <td>{{ c.dmwLicenseNumber || '—' }}
                <span v-if="c.dmwLicenseVerified" class="badge badge-verified">✓</span>
              </td>
              <td><span class="badge" :class="'status-' + c.verifiedStatus.toLowerCase()">{{ c.verifiedStatus }}</span></td>
              <td>{{ c.fleetSize || '—' }}</td>
              <td class="actions-cell">
                <button v-if="c.companyType === 'MANNING_AGENT' && !c.dmwLicenseVerified"
                        class="btn-xs btn-success" @click="verifyDmw(c.id)">Verify DMW</button>
              </td>
            </tr>
          </tbody>
        </table>
      </div>
    </section>
  </div>
</template>

<style scoped>
.admin-page { padding-top: var(--space-5); padding-bottom: var(--space-8); }
.admin-header { margin-bottom: var(--space-4); }
.admin-header h1 { font: var(--font-h1); color: var(--color-text-primary); }
.subtitle { font: var(--font-small); color: var(--color-text-secondary); }

.admin-tabs { display: flex; gap: var(--space-1); margin-bottom: var(--space-5); border-bottom: 1px solid var(--color-border); }
.tab { padding: var(--space-2) var(--space-4); font: var(--font-body); font-weight: 500; background: none; border: none;
  border-bottom: 2px solid transparent; cursor: pointer; color: var(--color-text-secondary); transition: all .15s; }
.tab.active { color: var(--color-primary); border-bottom-color: var(--color-primary); }
.tab:hover { color: var(--color-primary); }

.dashboard-grid { display: grid; grid-template-columns: repeat(4, 1fr); gap: var(--space-3); }
.stat-card { background: white; border: 1px solid var(--color-border); border-radius: var(--radius-md);
  padding: var(--space-4); text-align: center; }
.stat-value { font-size: 28px; font-weight: 600; color: var(--color-primary); }
.stat-label { font: var(--font-small); color: var(--color-text-secondary); margin-top: var(--space-1); }
.stat-card.seafarer .stat-value { color: var(--color-primary); }
.stat-card.owner .stat-value { color: var(--color-accent); }
.stat-card.agent .stat-value { color: var(--color-agent-green); }
.stat-card.success .stat-value { color: var(--color-success); }
.stat-card.warning .stat-value { color: var(--color-warning); }

.toolbar { display: flex; align-items: center; gap: var(--space-3); margin-bottom: var(--space-3); }
.result-count { font: var(--font-small); color: var(--color-text-secondary); }

.table-card { overflow-x: auto; padding: 0; }
.data-table { width: 100%; border-collapse: collapse; font: var(--font-small); }
.data-table th { text-align: left; padding: var(--space-3); font-weight: 500; color: var(--color-text-secondary);
  border-bottom: 1px solid var(--color-border); background: var(--color-surface); }
.data-table td { padding: var(--space-3); border-bottom: 1px solid var(--color-border); }
.data-table tr:hover { background: var(--color-surface); }
.company-name { font-weight: 500; }

.actions-cell { display: flex; gap: var(--space-1); }
.btn-xs { font-size: 11px; padding: 2px 8px; border-radius: 4px; border: none; cursor: pointer; font-weight: 500; }
.btn-success { background: var(--color-success-bg); color: var(--color-success); }
.btn-success:hover { background: var(--color-success); color: white; }
.btn-warning { background: var(--color-warning-bg); color: var(--color-warning); }
.btn-warning:hover { background: var(--color-warning); color: white; }

.badge-seafarer { background: var(--color-primary-light); color: var(--color-primary); }
.badge-shipowner { background: #FEF3E2; color: #B8860B; }
.badge-manning_agent { background: #E0F2F1; color: var(--color-agent-green); }
.badge-admin { background: #F3E5F5; color: #7B1FA2; }
.badge-manager { background: #E8EAF6; color: #3F51B5; }
.status-active { background: var(--color-success-bg); color: var(--color-success); }
.status-suspended { background: var(--color-danger-bg); color: var(--color-danger); }
.status-pending_verification { background: var(--color-warning-bg); color: var(--color-warning); }
.status-deactivated { background: var(--color-surface); color: var(--color-text-tertiary); }
.status-verified { background: var(--color-success-bg); color: var(--color-success); }
.status-unverified { background: var(--color-surface); color: var(--color-text-tertiary); }
.status-pending { background: var(--color-warning-bg); color: var(--color-warning); }

.loading-state { text-align: center; padding: var(--space-8); font: var(--font-body); color: var(--color-text-secondary); }

@media (max-width: 768px) { .dashboard-grid { grid-template-columns: repeat(2, 1fr); } }
</style>
