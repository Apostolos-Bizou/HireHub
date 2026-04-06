<script setup>
import { ref, computed } from 'vue'

const props = defineProps({
  showReviewButton: { type: Boolean, default: false }
})

const emit = defineEmits(['requestReview'])

const crewScore = ref({
  score: 87,
  totalReviews: 14,
  captainReviews: 3,
  verifiedVessels: 4,
  aiSummary: "Highly rated for safety awareness and teamwork. Colleagues consistently highlight his calm demeanor during emergencies and strong bridge team communication. Three Captains across different companies praise his reliability and work ethic. One recurring theme: exceptional mentoring of junior officers.",
  criteria: [
    { name: "Safety awareness", score: 4.6, weight: "25%", color: "#1B5E20" },
    { name: "Professional competence", score: 4.4, weight: "25%", color: "#0A66C2" },
    { name: "Teamwork & collaboration", score: 4.5, weight: "20%", color: "#1B5E20" },
    { name: "Reliability & work ethic", score: 4.3, weight: "20%", color: "#0A66C2" },
    { name: "Leadership & attitude", score: 4.2, weight: "10%", color: "#0A66C2" }
  ],
  reviews: [
    {
      id: 1,
      authorName: "Capt. Georgios Papandreou",
      authorInitials: "GP",
      authorRank: "Master",
      vessel: "MT Aegean Warrior",
      duration: "14 months together",
      rating: 5,
      text: "Outstanding Chief Officer. His cargo handling expertise is exceptional — zero incidents during our entire contract. Always the first to volunteer for drills and the last to leave the bridge during critical maneuvers. Calm under pressure, excellent communicator with the crew. I would sail with him again without hesitation.",
      avatarColor: "#0A66C2",
      date: "Feb 2026"
    },
    {
      id: 2,
      authorName: "Ruslan Goncharov",
      authorInitials: "RG",
      authorRank: "2nd Officer",
      vessel: "MT Mediterranean Star",
      duration: "8 months together",
      rating: 4,
      text: "Very good officer to work with. Taught me a lot about ECDIS route planning and tanker operations. Supportive and patient with junior officers. Sometimes pushes hard on procedures but that's exactly what makes him reliable. Strong recommendation.",
      avatarColor: "#E7A33E",
      date: "Dec 2024"
    },
    {
      id: 3,
      authorName: "Andrei Marinescu",
      authorInitials: "AM",
      authorRank: "3rd Officer",
      vessel: "MT Coral Bay",
      duration: "6 months together",
      rating: 5,
      text: "Best mentor I've had on board. He spent time after watch teaching me cargo calculations and bridge procedures. Always positive, always professional. The kind of officer every cadet dreams of working under.",
      avatarColor: "#1D9E75",
      date: "Jan 2023"
    }
  ]
})

function scoreColor(s) {
  if (s >= 80) return '#1B5E20'
  if (s >= 60) return '#0A66C2'
  return '#E7A33E'
}

function stars(n) {
  return '★'.repeat(n) + '☆'.repeat(5 - n)
}

const showAll = ref(false)
const visibleReviews = computed(() => showAll.value ? crewScore.value.reviews : crewScore.value.reviews.slice(0, 3))
</script>

<template>
  <div class="crewscore-widget">
    <!-- Score Hero -->
    <div class="cs-hero">
      <div class="cs-ring" :style="{ borderColor: scoreColor(crewScore.score) }">
        <span class="cs-num" :style="{ color: scoreColor(crewScore.score) }">{{ crewScore.score }}</span>
        <span class="cs-of">/ 100</span>
      </div>
      <div class="cs-details">
        <div class="cs-title">CrewScore</div>
        <div class="cs-subtitle">Based on {{ crewScore.totalReviews }} verified peer reviews across {{ crewScore.verifiedVessels }} vessels</div>
        <div class="cs-badges">
          <span class="cs-badge cs-badge-green">{{ crewScore.totalReviews }} reviews</span>
          <span class="cs-badge cs-badge-green">{{ crewScore.captainReviews }} captains reviewed</span>
          <span class="cs-badge cs-badge-blue">{{ crewScore.verifiedVessels }} vessels verified</span>
        </div>
      </div>
    </div>

    <!-- AI Summary -->
    <div class="cs-ai-summary">
      <div class="cs-ai-label">
        <svg xmlns="http://www.w3.org/2000/svg" width="12" height="12" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round"><path d="M12 2a4 4 0 0 1 4 4c0 1.95-1.4 3.58-3.25 3.93L12 22"/><path d="M12 2a4 4 0 0 0-4 4c0 1.95 1.4 3.58 3.25 3.93"/><circle cx="12" cy="6" r="1.5"/></svg>
        AI crew analysis
      </div>
      {{ crewScore.aiSummary }}
    </div>

    <!-- Criteria Bars -->
    <div class="cs-criteria">
      <div v-for="c in crewScore.criteria" :key="c.name" class="cs-crit-item">
        <span class="cs-crit-name">{{ c.name }} <span class="cs-crit-weight">({{ c.weight }})</span></span>
        <div class="cs-crit-right">
          <div class="cs-crit-bar">
            <div class="cs-crit-fill" :style="{ width: (c.score / 5 * 100) + '%', background: c.color }"></div>
          </div>
          <span class="cs-crit-val" :style="{ color: c.color }">{{ c.score }}</span>
        </div>
      </div>
    </div>

    <!-- Review Button -->
    <div v-if="showReviewButton" class="cs-review-action">
      <button class="btn btn-secondary" @click="emit('requestReview')">
        <svg xmlns="http://www.w3.org/2000/svg" width="14" height="14" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round"><path d="M11 4H4a2 2 0 0 0-2 2v14a2 2 0 0 0 2 2h14a2 2 0 0 0 2-2v-7"/><path d="M18.5 2.5a2.121 2.121 0 0 1 3 3L12 15l-4 1 1-4 9.5-9.5z"/></svg>
        Write a review
      </button>
    </div>

    <!-- Reviews -->
    <div class="cs-reviews-header">
      <h3>Peer reviews ({{ crewScore.totalReviews }})</h3>
    </div>

    <div v-for="r in visibleReviews" :key="r.id" class="cs-review">
      <div class="cs-rev-header">
        <div class="cs-rev-author">
          <div class="cs-rev-avatar" :style="{ background: r.avatarColor }">{{ r.authorInitials }}</div>
          <div>
            <div class="cs-rev-name">{{ r.authorName }}</div>
            <div class="cs-rev-meta">{{ r.authorRank }} · {{ r.vessel }} · {{ r.duration }}</div>
          </div>
        </div>
        <div class="cs-rev-right">
          <span class="cs-rev-verified">Verified colleague</span>
          <span class="cs-rev-date">{{ r.date }}</span>
        </div>
      </div>
      <div class="cs-rev-stars">{{ stars(r.rating) }}</div>
      <div class="cs-rev-text">{{ r.text }}</div>
    </div>

    <button v-if="crewScore.reviews.length > 3 && !showAll" class="cs-show-more" @click="showAll = true">
      Show all {{ crewScore.totalReviews }} reviews
    </button>
  </div>
</template>

<style scoped>
.crewscore-widget { margin-bottom: var(--space-4); }

.cs-hero {
  display: flex;
  align-items: center;
  gap: var(--space-5);
  padding: var(--space-4);
  background: var(--color-white);
  border: 0.5px solid var(--color-border);
  border-radius: var(--radius-lg);
  margin-bottom: var(--space-3);
}
.cs-ring {
  width: 88px;
  height: 88px;
  border-radius: 50%;
  border: 4px solid;
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  flex-shrink: 0;
}
.cs-num { font-size: 28px; font-weight: 600; }
.cs-of { font-size: 11px; color: var(--color-text-tertiary); }
.cs-details { flex: 1; }
.cs-title { font: var(--font-h3); margin-bottom: 4px; }
.cs-subtitle { font: var(--font-small); color: var(--color-text-secondary); margin-bottom: var(--space-2); }
.cs-badges { display: flex; gap: 6px; flex-wrap: wrap; }
.cs-badge { font: var(--font-caption); font-weight: 500; padding: 3px 8px; border-radius: 4px; }
.cs-badge-green { background: var(--color-success-bg); color: var(--color-success); }
.cs-badge-blue { background: var(--color-primary-light); color: var(--color-primary); }

.cs-ai-summary {
  padding: var(--space-3) var(--space-4);
  background: var(--color-primary-light);
  border-radius: var(--radius-md);
  margin-bottom: var(--space-3);
  font: var(--font-small);
  line-height: 1.7;
  color: var(--color-primary-dark);
  border-left: 3px solid var(--color-primary);
}
.cs-ai-label {
  display: flex;
  align-items: center;
  gap: 6px;
  font: var(--font-caption);
  font-weight: 600;
  color: var(--color-primary);
  text-transform: uppercase;
  letter-spacing: 0.5px;
  margin-bottom: 6px;
}

.cs-criteria {
  display: grid;
  grid-template-columns: 1fr 1fr;
  gap: var(--space-2);
  margin-bottom: var(--space-3);
}
.cs-crit-item {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: var(--space-2) var(--space-3);
  background: var(--color-surface);
  border-radius: var(--radius-md);
}
.cs-crit-name { font: var(--font-caption); color: var(--color-text-secondary); }
.cs-crit-weight { color: var(--color-text-tertiary); }
.cs-crit-right { display: flex; align-items: center; gap: 6px; }
.cs-crit-bar { width: 60px; height: 6px; background: var(--color-border); border-radius: 3px; overflow: hidden; }
.cs-crit-fill { height: 100%; border-radius: 3px; }
.cs-crit-val { font: var(--font-caption); font-weight: 600; min-width: 24px; text-align: right; }

.cs-review-action { margin-bottom: var(--space-3); }

.cs-reviews-header {
  margin-bottom: var(--space-3);
}
.cs-reviews-header h3 { font: var(--font-h3); }

.cs-review {
  padding: var(--space-4);
  background: var(--color-white);
  border: 0.5px solid var(--color-border);
  border-radius: var(--radius-lg);
  margin-bottom: var(--space-2);
}
.cs-rev-header {
  display: flex;
  justify-content: space-between;
  align-items: flex-start;
  margin-bottom: var(--space-2);
}
.cs-rev-author { display: flex; align-items: center; gap: var(--space-2); }
.cs-rev-avatar {
  width: 36px;
  height: 36px;
  border-radius: 50%;
  color: white;
  display: flex;
  align-items: center;
  justify-content: center;
  font-size: 13px;
  font-weight: 500;
  flex-shrink: 0;
}
.cs-rev-name { font: var(--font-body); font-weight: 500; }
.cs-rev-meta { font: var(--font-caption); color: var(--color-text-tertiary); }
.cs-rev-right { display: flex; flex-direction: column; align-items: flex-end; gap: 2px; }
.cs-rev-verified {
  font: var(--font-caption);
  font-weight: 500;
  padding: 2px 6px;
  background: var(--color-success-bg);
  color: var(--color-success);
  border-radius: 4px;
}
.cs-rev-date { font: var(--font-caption); color: var(--color-text-tertiary); }
.cs-rev-stars { color: #E7A33E; font-size: 14px; letter-spacing: 1px; margin-bottom: 6px; }
.cs-rev-text { font: var(--font-small); line-height: 1.7; color: var(--color-text-secondary); }

.cs-show-more {
  display: block;
  width: 100%;
  padding: var(--space-3);
  background: none;
  border: 1px solid var(--color-border);
  border-radius: var(--radius-md);
  font: var(--font-small);
  font-weight: 500;
  color: var(--color-primary);
  cursor: pointer;
  text-align: center;
}
.cs-show-more:hover { background: var(--color-primary-light); }

@media (max-width: 768px) {
  .cs-hero { flex-direction: column; text-align: center; }
  .cs-badges { justify-content: center; }
  .cs-criteria { grid-template-columns: 1fr; }
  .cs-rev-header { flex-direction: column; gap: var(--space-2); }
  .cs-rev-right { align-items: flex-start; }
}
</style>
