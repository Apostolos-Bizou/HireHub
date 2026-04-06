<script setup>
import { ref } from 'vue'

const activeSection = ref(0)
const sections = [
  { name: 'Εισαγωγή', nameEn: 'Introduction' },
  { name: 'Πώς λειτουργεί', nameEn: 'How it works' },
  { name: 'Πλοιοκτήτης', nameEn: 'Shipowner' },
  { name: 'Manning Agent', nameEn: 'Manning Agent' },
  { name: 'Ναυτικός', nameEn: 'Seafarer' },
  { name: 'CrewScore', nameEn: 'CrewScore' },
  { name: 'AI Εργαλεία', nameEn: 'AI Tools' },
  { name: 'Salary', nameEn: 'Salary Benchmark' },
  { name: 'Γλωσσάριο', nameEn: 'Glossary' }
]

const glossary = [
  { term: 'AI Score', desc: 'Βαθμολογία τεχνητής νοημοσύνης που δείχνει πόσο ταιριάζει ένας ναυτικός με τα κριτήρια αναζήτησης. Υπολογίζεται αυτόματα βάσει εμπειρίας, πιστοποιητικών, και τύπου πλοίου.', descEn: 'AI-calculated score showing how well a seafarer matches search criteria based on experience, certificates, and vessel type.' },
  { term: 'CrewScore', desc: 'Βαθμολογία 0-100 από αξιολογήσεις συναδέλφων σε 5 κριτήρια: Ασφάλεια, Ικανότητα, Ομαδικότητα, Αξιοπιστία, Ηγεσία. Μόνο verified αξιολογήσεις από συναδέλφους στο ίδιο πλοίο.', descEn: 'Peer review score (0-100) across 5 criteria: Safety, Competence, Teamwork, Reliability, Leadership. Only verified reviews from colleagues on the same vessel.' },
  { term: 'Deployment Readiness (DR)', desc: 'Βαθμός ετοιμότητας ενός ναυτικού για επιβίβαση. Ελέγχει: πιστοποιητικά, σημαίες, ιατρικά, διαθεσιμότητα. Πράσινο = έτοιμος, κίτρινο = χρειάζεται ενέργεια, κόκκινο = δεν μπορεί.', descEn: 'Readiness score for deployment. Checks: certificates, flags, medical, availability. Green = ready, yellow = needs action, red = cannot deploy.' },
  { term: 'Shortlist', desc: 'Λίστα υποψήφιων ναυτικών που δημιουργεί ο πλοιοκτήτης για μια συγκεκριμένη θέση. Η shortlist προωθείται στον manning agent για εκτέλεση.', descEn: 'Candidate list created by shipowner for a specific position. The shortlist is forwarded to the manning agent for execution.' },
  { term: 'Manning Agent', desc: 'Αδειοδοτημένος πράκτορας στελέχωσης (DMW license) που εκτελεί σύμβαση, ιατρικά, visa, και deployment. Ο μεσάζοντας μεταξύ πλοιοκτήτη και ναυτικού.', descEn: 'Licensed manning agent (DMW) who executes contracts, medical, visa, and deployment. The intermediary between shipowner and seafarer.' },
  { term: 'DMW', desc: 'Department of Migrant Workers — Φιλιππινέζικη κρατική υπηρεσία που αδειοδοτεί manning agencies και εποπτεύει την αποστολή ναυτικών στο εξωτερικό.', descEn: 'Department of Migrant Workers — Philippine government agency that licenses manning agencies and oversees overseas deployment.' },
  { term: 'STCW', desc: 'Standards of Training, Certification and Watchkeeping — Διεθνή πρότυπα εκπαίδευσης και πιστοποίησης ναυτικών. Υποχρεωτικό για όλους.', descEn: 'Standards of Training, Certification and Watchkeeping — International training and certification standards for seafarers.' },
  { term: 'COC', desc: 'Certificate of Competency — Πιστοποιητικό ικανότητας ναυτικού. Αποδεικνύει ότι μπορεί να ασκεί συγκεκριμένο βαθμό (π.χ. 3rd Officer, Chief Engineer).', descEn: 'Certificate of Competency — Proves the seafarer is qualified to serve at a specific rank.' },
  { term: 'Flag Endorsement', desc: 'Έγκριση σημαίας — Κάθε πλοίο έχει σημαία (π.χ. Panama, Marshall Islands). Ο ναυτικός χρειάζεται endorsement από τη σημαία για να δουλέψει σε εκείνο το πλοίο.', descEn: 'Flag state endorsement — Each vessel has a flag. Seafarers need endorsement from that flag state to serve on board.' },
  { term: 'PEME', desc: 'Pre-Employment Medical Examination — Ιατρική εξέταση πριν την τοποθέτηση. Υποχρεωτική για κάθε deployment.', descEn: 'Pre-Employment Medical Examination — Required medical check before each deployment.' },
  { term: 'OEC', desc: 'Overseas Employment Certificate — Πιστοποιητικό που εκδίδει η DMW και επιτρέπει στον ναυτικό να αναχωρήσει νόμιμα.', descEn: 'Overseas Employment Certificate — DMW-issued certificate allowing legal departure.' },
  { term: 'DWT', desc: 'Deadweight Tonnage — Η χωρητικότητα φορτίου ενός πλοίου σε τόνους. Δείκτης μεγέθους πλοίου.', descEn: 'Deadweight Tonnage — A vessel cargo capacity in tonnes. Indicator of vessel size.' },
  { term: 'IMO', desc: 'International Maritime Organization — Ο διεθνής ναυτιλιακός οργανισμός που θεσπίζει κανονισμούς ασφάλειας, περιβάλλοντος, και εκπαίδευσης.', descEn: 'International Maritime Organization — Sets global standards for safety, environment, and training.' },
  { term: 'Principal', desc: 'Ο πλοιοκτήτης ή η εταιρεία διαχείρισης πλοίων που χρησιμοποιεί τον manning agent. Ένας agent μπορεί να εξυπηρετεί πολλούς principals.', descEn: 'The shipowner or management company using the manning agent. An agent can serve multiple principals.' },
  { term: 'Gantt Chart', desc: 'Οπτικό χρονοδιάγραμμα με μπάρες που δείχνει ποιος ναυτικός είναι σε ποιο πλοίο, πότε λήγει, και ποιος τον αντικαθιστά.', descEn: 'Visual timeline with bars showing which seafarer is on which vessel, when contracts expire, and planned reliefs.' },
  { term: 'Crew Change', desc: 'Η αντικατάσταση πληρώματος — ένας ναυτικός αποβιβάζεται, ο αντικαταστάτης επιβιβάζεται. Κρίσιμη λειτουργία για τη συνεχή λειτουργία του πλοίου.', descEn: 'Crew replacement — one seafarer disembarks, the replacement embarks. Critical for continuous vessel operation.' },
  { term: 'Salary Benchmark', desc: 'Εργαλείο σύγκρισης μισθών ανά βαθμό, τύπο πλοίου, εθνικότητα και εμπειρία. Δείχνει min, median, max και εξέλιξη μισθών. Χρήσιμο για ναυτικούς, πλοιοκτήτες και agents.', descEn: 'Salary comparison tool by rank, vessel type, nationality and experience. Shows min, median, max and salary trends. Useful for seafarers, shipowners and agents.' },
]
</script>

<template>
  <div class="container guide-page">
    <div class="guide-header">
      <h1>
        <svg xmlns="http://www.w3.org/2000/svg" width="24" height="24" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="1.8"><path d="M2 3h6a4 4 0 0 1 4 4v14a3 3 0 0 0-3-3H2z"/><path d="M22 3h-6a4 4 0 0 0-4 4v14a3 3 0 0 1 3-3h7z"/></svg>
        Οδηγός Πλατφόρμας
      </h1>
      <p class="guide-subtitle">Platform Guide — Μάθε πώς λειτουργεί το HireHub</p>
    </div>

    <!-- Section Tabs -->
    <div class="guide-tabs">
      <button v-for="(s, i) in sections" :key="i" class="guide-tab" :class="{ active: activeSection === i }" @click="activeSection = i">
        {{ s.name }}
      </button>
    </div>

    <!-- Section 0: Εισαγωγή -->
    <div v-if="activeSection === 0" class="guide-content">
      <div class="card guide-card highlight-card">
        <h2>Τι είναι το HireHub;</h2>
        <p class="lead">Ένας πλήρης οδηγός για αρχάριους και έμπειρους χρήστες</p>
      </div>
      <div class="card guide-card">
        <p>Το <strong>HireHub</strong> είναι η πρώτη πλατφόρμα επαγγελματικής δικτύωσης αποκλειστικά για ναυτικούς — σκεφτείτε το σαν το <strong>LinkedIn της ναυτιλίας</strong>.</p>
        <p>Συνδέει τρεις κόσμους:</p>
        <div class="three-cards">
          <div class="role-card role-seafarer">
            <div class="role-icon">
              <svg xmlns="http://www.w3.org/2000/svg" width="28" height="28" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="1.5"><path d="M20 21v-2a4 4 0 0 0-4-4H8a4 4 0 0 0-4 4v2"/><circle cx="12" cy="7" r="4"/></svg>
            </div>
            <h4>Ναυτικός</h4>
            <p>Δημιουργεί προφίλ, λαμβάνει αξιολογήσεις, γίνεται ορατός σε πλοιοκτήτες παγκοσμίως</p>
          </div>
          <div class="role-card role-owner">
            <div class="role-icon">
              <svg xmlns="http://www.w3.org/2000/svg" width="28" height="28" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="1.5"><path d="M2 20l2-2 2 2 2-2 2 2 2-2 2 2 2-2 2 2 2-2 2 2"/><path d="M4 18V8a2 2 0 0 1 2-2h12a2 2 0 0 1 2 2v10"/></svg>
            </div>
            <h4>Πλοιοκτήτης</h4>
            <p>Αναζητά talent, βλέπει BI analytics, δημιουργεί shortlists και τις στέλνει στον agent</p>
          </div>
          <div class="role-card role-agent">
            <div class="role-icon">
              <svg xmlns="http://www.w3.org/2000/svg" width="28" height="28" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="1.5"><path d="M2 20h20"/><path d="M5 20V8l7-5 7 5v12"/><path d="M9 20v-6h6v6"/></svg>
            </div>
            <h4>Manning Agent</h4>
            <p>Λαμβάνει υποψηφίους, διαχειρίζεται deployment, χρησιμοποιεί AI εργαλεία</p>
          </div>
        </div>
        <div class="info-box">
          <strong>Σημαντικό:</strong> Το HireHub δεν είναι γραφείο εύρεσης εργασίας. Δεν κάνει recruitment. Είναι πλατφόρμα επαγγελματικής ορατότητας. Η πρόσληψη γίνεται πάντα μέσω αδειοδοτημένου manning agent (DMW license).
        </div>
      </div>
    </div>

    <!-- Section 1: Πώς λειτουργεί -->
    <div v-if="activeSection === 1" class="guide-content">
      <div class="card guide-card highlight-card">
        <h2>Πώς λειτουργεί το HireHub;</h2>
        <p class="lead">Η ροή σε 5 βήματα — από το προφίλ μέχρι το πλοίο</p>
      </div>
      <div class="card guide-card">
        <div class="steps">
          <div class="step">
            <div class="step-num">1</div>
            <div class="step-content">
              <h4>Ο ναυτικός δημιουργεί προφίλ</h4>
              <p>Ανεβάζει πιστοποιητικά (COC, STCW), θαλάσσια υπηρεσία, δεξιότητες. Το AI σκανάρει και επαληθεύει τα έγγραφα. Λαμβάνει <strong>Verified badge</strong>.</p>
              <p class="step-en">Seafarer creates verified profile with certificates, sea service, and skills.</p>
            </div>
          </div>
          <div class="step">
            <div class="step-num">2</div>
            <div class="step-content">
              <h4>Ο πλοιοκτήτης αναζητά</h4>
              <p>Χρησιμοποιεί φίλτρα (βαθμός, τύπος πλοίου, εθνικότητα, διαθεσιμότητα) και AI matching. Βλέπει <strong>AI Score + CrewScore</strong> δίπλα-δίπλα.</p>
              <p class="step-en">Shipowner searches using filters and AI matching. Sees dual scores.</p>
            </div>
          </div>
          <div class="step">
            <div class="step-num">3</div>
            <div class="step-content">
              <h4>Δημιουργία Shortlist</h4>
              <p>Ο πλοιοκτήτης επιλέγει τους καλύτερους υποψηφίους και τους βάζει σε shortlist. Μπορεί να τους συγκρίνει side-by-side.</p>
              <p class="step-en">Shipowner creates a shortlist of top candidates and can compare them.</p>
            </div>
          </div>
          <div class="step">
            <div class="step-num">4</div>
            <div class="step-content">
              <h4>Προώθηση σε Manning Agent</h4>
              <p>Η shortlist στέλνεται στον DMW-licensed manning agent. Ο agent βλέπει <strong>Deployment Readiness Score</strong> ανά υποψήφιο.</p>
              <p class="step-en">Shortlist forwarded to DMW-licensed manning agent with readiness scores.</p>
            </div>
          </div>
          <div class="step">
            <div class="step-num step-last">5</div>
            <div class="step-content">
              <h4>Deployment & Αξιολόγηση</h4>
              <p>Ο agent εκτελεί σύμβαση, ιατρικά, ταξίδι. Μετά το τέλος, οι συνάδελφοι αξιολογούν μεταξύ τους μέσω <strong>CrewScore</strong>.</p>
              <p class="step-en">Agent handles contract and deployment. After voyage, peers rate each other via CrewScore.</p>
            </div>
          </div>
        </div>
      </div>
    </div>

    <!-- Section 2: Πλοιοκτήτης -->
    <div v-if="activeSection === 2" class="guide-content">
      <div class="card guide-card highlight-card highlight-owner">
        <h2>Οδηγός Πλοιοκτήτη</h2>
        <p class="lead">Τι βλέπει και τι κάνει ο Shipowner / Fleet Manager</p>
      </div>
      <div class="card guide-card">
        <h3>BI Dashboard (Αρχική σελίδα)</h3>
        <p>Μόλις συνδεθείτε, βλέπετε το Business Intelligence Dashboard:</p>
        <div class="feature-list">
          <div class="feat"><strong>8 KPI Cards:</strong> Εγγεγραμμένοι ναυτικοί, verified profiles, avg CrewScore, peer reviews, shortlists, candidates forwarded, placements, time to hire</div>
          <div class="feat"><strong>Crew Availability Timeline:</strong> Διαδραστικό εργαλείο 10-60 ημερών. Πατάτε 10/20/30/40/50/60 και βλέπετε πόσοι ναυτικοί είναι διαθέσιμοι, ανά βαθμό, ανά εθνικότητα. Κλικ σε rank = βλέπετε τους υποψηφίους.</div>
          <div class="feat"><strong>Crew Availability by Rank:</strong> Μπάρες ανά ειδικότητα — Master, Chief Officer, Engineers, AB, κλπ</div>
          <div class="feat"><strong>Top Nationalities:</strong> Κατανομή ναυτικών ανά εθνικότητα με ποσοστά</div>
          <div class="feat"><strong>Monthly Placements:</strong> Γράφημα τάσης — πόσες τοποθετήσεις και shortlists ανά μήνα</div>
          <div class="feat"><strong>Vessel Type Demand:</strong> Ζήτηση ανά τύπο πλοίου (tanker, bulk, container κλπ)</div>
          <div class="feat"><strong>Alerts:</strong> Επείγουσες ειδοποιήσεις — λήξεις, νέα profiles, deployments</div>
        </div>
      </div>
      <div class="card guide-card">
        <h3>Talent Search</h3>
        <p>Αναζήτηση ναυτικών με φίλτρα: βαθμός, τύπος πλοίου, θαλάσσια υπηρεσία, εθνικότητα, διαθεσιμότητα, verified only, min CrewScore. Τα αποτελέσματα εμφανίζονται με <strong>δύο score rings</strong>: AI (μπλε) + CrewScore (πράσινο).</p>
      </div>
      <div class="card guide-card">
        <h3>AI Recruit</h3>
        <p>Αυτόματη σύσταση υποψηφίων βάσει AI matching. Φίλτρα rank + vessel type → Top 5 αποτελέσματα ταξινομημένα με dual scoring. Ο AI λαμβάνει υπόψη εμπειρία, πιστοποιητικά, CrewScore, διαθεσιμότητα.</p>
      </div>
      <div class="card guide-card">
        <h3>Shortlist & Pipeline</h3>
        <p>Δημιουργήστε shortlists ανά θέση (π.χ. "3rd Officer — Oil Tanker"). Κάθε shortlist δείχνει τους υποψηφίους με status (Shortlisted → Under Review → Sent to Agent). Πάνω φαίνεται ο connected manning agent με DMW license number. Κουμπί "Forward to Agent" = αποστολή.</p>
      </div>
    </div>

    <!-- Section 3: Manning Agent -->
    <div v-if="activeSection === 3" class="guide-content">
      <div class="card guide-card highlight-card highlight-agent">
        <h2>Οδηγός Manning Agent</h2>
        <p class="lead">AI-powered εργαλεία deployment — 6 tabs, 5 AI sub-tabs</p>
      </div>
      <div class="card guide-card">
        <h3>Agent Dashboard (6 Tabs)</h3>
        <div class="feature-list">
          <div class="feat"><strong>Crew Calendar:</strong> Gantt chart ανά πλοίο ανά principal. Dropdown αλλαγής εταιρείας + πλοίου. Βλέπετε ποιος είναι πάνω, πότε λήγει, ποιος τον αντικαθιστά. Μπλε = active, πορτοκαλί = expiring, κόκκινο = expired, πράσινο = planned.</div>
          <div class="feat"><strong>AI Intelligence (5 sub-tabs):</strong>
            <ul class="sub-list">
              <li><strong>Cost Calculator:</strong> Επιλέγετε candidate, το AI υπολογίζει αυτόματα κόστος (ιατρικά, ταξίδι, σημαία, visa, DMW, ασφάλιση, agency fee, ανανεώσεις)</li>
              <li><strong>Compare Candidates:</strong> Σύγκριση 3 υποψηφίων πλάι-πλάι σε 13 κριτήρια με χρωματική κωδικοποίηση</li>
              <li><strong>Deploy Timeline:</strong> Βήμα-βήμα χρονοδιάγραμμα από shortlist μέχρι επιβίβαση</li>
              <li><strong>Principal Intel:</strong> Τι προτιμάει κάθε πλοιοκτήτης + ιστορικό τοποθετήσεων</li>
              <li><strong>Auto-Checklist:</strong> 9-step λίστα ελέγχου deployment (done/in-progress/pending)</li>
            </ul>
          </div>
          <div class="feat"><strong>Principal Matching:</strong> AI αντιστοιχεί νέους ναυτικούς σε πλοιοκτήτες βάσει μαθημένων προτιμήσεων (εθνικότητα, εμπειρία, CrewScore)</div>
          <div class="feat"><strong>Shortlist Analyzer:</strong> Εισερχόμενα αιτήματα από πλοιοκτήτες με AI ανάλυση ετοιμότητας ανά υποψήφιο</div>
          <div class="feat"><strong>Deployment Readiness:</strong> Traffic light checklist — πιστοποιητικά, σημαίες, ιατρικά, διαθεσιμότητα. Τρία scores: AI + CrewScore + DR</div>
          <div class="feat"><strong>Compliance:</strong> Ειδοποιήσεις (ληγμένα πιστοποιητικά, DMW license), pipeline performance, bottleneck analysis</div>
        </div>
      </div>
      <div class="card guide-card">
        <h3>Talent Search & AI Recruit</h3>
        <p>Ο Agent έχει τα ίδια εργαλεία αναζήτησης με τον πλοιοκτήτη — αλλά τα χρησιμοποιεί για <strong>πολλούς principals ταυτόχρονα</strong>. Αναζητά ναυτικούς, βλέπει dual scores, και τους αντιστοιχεί στον κατάλληλο πλοιοκτήτη.</p>
      </div>
    </div>

    <!-- Section 4: Ναυτικός -->
    <div v-if="activeSection === 4" class="guide-content">
      <div class="card guide-card highlight-card highlight-seafarer">
        <h2>Οδηγός Ναυτικού</h2>
        <p class="lead">Γίνε ορατός σε πλοιοκτήτες — αξιοκρατία, όχι βύσμα</p>
      </div>
      <div class="card guide-card">
        <h3>Το Προφίλ σου</h3>
        <div class="feature-list">
          <div class="feat"><strong>Hero Card:</strong> Φωτογραφία, βαθμός, τύπος πλοίου, χρόνια υπηρεσίας, Verified badge</div>
          <div class="feat"><strong>Θαλάσσια Υπηρεσία:</strong> Timeline με κάθε πλοίο — βαθμός, τύπος, DWT, σημαία, ημερομηνίες</div>
          <div class="feat"><strong>Πιστοποιητικά:</strong> COC, STCW, COP, flag endorsements — με status Valid/Expiring. AI verification badge</div>
          <div class="feat"><strong>Δεξιότητες:</strong> ECDIS, GMDSS, Cargo Operations, ISM/ISPS, Tanker Safety κλπ</div>
          <div class="feat"><strong>CrewScore:</strong> Η βαθμολογία σου από αξιολογήσεις συναδέλφων (βλ. ενότητα CrewScore)</div>
          <div class="feat"><strong>Διαθεσιμότητα:</strong> Πότε είσαι διαθέσιμος, τι σύμβαση προτιμάς, σε τι πλοίο</div>
        </div>
      </div>
      <div class="card guide-card">
        <h3>Ιδιωτικότητα</h3>
        <p>Ο πλοιοκτήτης και ο agent βλέπουν <strong>μόνο</strong> επαγγελματικά στοιχεία: βαθμό, υπηρεσία, πιστοποιητικά, CrewScore. <strong>Δεν</strong> βλέπουν: διαβατήριο, ιατρικά, έγγραφα, προσωπικά στοιχεία. Εσύ ελέγχεις τι μοιράζεσαι.</p>
      </div>
      <div class="card guide-card">
        <h3>Community Feed</h3>
        <p>LinkedIn-style feed με ναυτιλιακό περιεχόμενο: εκπαιδεύσεις, καριέρα, νέα, trending θέματα (ECDIS 2026, Red Sea routing, Decarbonization). Μοιράσου τις εμπειρίες σου.</p>
      </div>
    </div>

    <!-- Section 5: CrewScore -->
    <div v-if="activeSection === 5" class="guide-content">
      <div class="card guide-card highlight-card">
        <h2>CrewScore — Σύστημα Αξιολόγησης Συναδέλφων</h2>
        <p class="lead">Η πιο μοναδική λειτουργία του HireHub — κανένας ανταγωνιστής δεν την έχει</p>
      </div>
      <div class="card guide-card">
        <h3>Τι είναι;</h3>
        <p>Ένα σύστημα αξιολόγησης <strong>peer-to-peer</strong>. Μόνο ναυτικοί που δούλεψαν μαζί στο <strong>ίδιο πλοίο</strong> μπορούν να αξιολογήσουν ο ένας τον άλλον. Αυτό εγγυάται αυθεντικότητα.</p>
        <h3>5 Κριτήρια</h3>
        <div class="criteria-grid">
          <div class="criteria-item"><strong>Ασφάλεια (Safety) — 25%</strong><p>Τήρηση κανόνων ασφαλείας, ISM, γκέτες, ασκήσεις</p></div>
          <div class="criteria-item"><strong>Ικανότητα (Competence) — 25%</strong><p>Τεχνικές δεξιότητες, γνώση, επαγγελματισμός</p></div>
          <div class="criteria-item"><strong>Ομαδικότητα (Teamwork) — 20%</strong><p>Συνεργασία, επικοινωνία, κλίμα στο πλοίο</p></div>
          <div class="criteria-item"><strong>Αξιοπιστία (Reliability) — 20%</strong><p>Συνέπεια, ευθύνη, τήρηση υποχρεώσεων</p></div>
          <div class="criteria-item"><strong>Ηγεσία (Leadership) — 10%</strong><p>Ικανότητα καθοδήγησης, λήψη αποφάσεων, mentoring</p></div>
        </div>
        <h3>Πώς χρησιμοποιείται;</h3>
        <p>Ο πλοιοκτήτης βλέπει το <strong>CrewScore δίπλα στο AI Score</strong> σε κάθε αναζήτηση. Έτσι ξέρει όχι μόνο αν ο ναυτικός έχει τα σωστά πιστοποιητικά (AI Score), αλλά και πώς είναι <strong>πραγματικά στο πλοίο</strong> (CrewScore).</p>
      </div>
    </div>

    <!-- Section 6: AI Εργαλεία -->
    <div v-if="activeSection === 6" class="guide-content">
      <div class="card guide-card highlight-card">
        <h2>AI Εργαλεία — Τεχνητή Νοημοσύνη</h2>
        <p class="lead">Πώς η τεχνητή νοημοσύνη βοηθάει κάθε ρόλο</p>
      </div>
      <div class="card guide-card">
        <h3>Για τον Πλοιοκτήτη</h3>
        <div class="feature-list">
          <div class="feat"><strong>AI Matching:</strong> Ο αλγόριθμος βαθμολογεί κάθε ναυτικό βάσει: εμπειρία, πιστοποιητικά, τύπος πλοίου, εθνικότητα, διαθεσιμότητα</div>
          <div class="feat"><strong>AI Crew Recommender:</strong> Αυτόματη πρόταση Top 5 υποψηφίων με dual scoring (AI + CrewScore)</div>
          <div class="feat"><strong>Availability Timeline:</strong> AI προβλέπει τη διαθεσιμότητα ανά 10-60 ημέρες</div>
        </div>
      </div>
      <div class="card guide-card">
        <h3>Για τον Manning Agent</h3>
        <div class="feature-list">
          <div class="feat"><strong>Cost Calculator:</strong> Αυτόματος υπολογισμός κόστους deployment ανά υποψήφιο (βάσει εθνικότητας, προέλευσης, κατάστασης εγγράφων)</div>
          <div class="feat"><strong>Candidate Compare:</strong> Side-by-side σύγκριση σε 13 κριτήρια με AI verdict</div>
          <div class="feat"><strong>Deployment Timeline:</strong> AI χρονοδιάγραμμα βήμα-βήμα</div>
          <div class="feat"><strong>Principal Matching:</strong> AI μαθαίνει τις προτιμήσεις κάθε πλοιοκτήτη και αυτόματα προτείνει ναυτικούς</div>
          <div class="feat"><strong>Deployment Readiness:</strong> Αυτόματος έλεγχος πιστοποιητικών, σημαιών, ιατρικών — DR score 0-100</div>
        </div>
      </div>
      <div class="card guide-card">
        <h3>Για τον Ναυτικό</h3>
        <div class="feature-list">
          <div class="feat"><strong>AI Certificate Verification:</strong> Αυτόματη σάρωση και επαλήθευση πιστοποιητικών</div>
          <div class="feat"><strong>CrewScore AI Summary:</strong> AI ανάλυση sentiment από τα σχόλια αξιολογήσεων</div>
          <div class="feat"><strong>Profile Completeness:</strong> AI υποδεικνύει τι λείπει για πλήρες προφίλ</div>
        </div>
      </div>
    </div>

    <!-- Section 7: Salary Benchmark -->
    <div v-if="activeSection === 7" class="guide-content">
      <div class="card guide-card highlight-card">
        <h2>Salary Benchmark — Μισθολογικά Δεδομένα</h2>
        <p class="lead">Σύγκριση μισθών ανά βαθμό, τύπο πλοίου, εθνικότητα και εμπειρία</p>
      </div>
      <div class="card guide-card">
        <p>Το <strong>Salary Benchmark</strong> είναι εργαλείο market intelligence που δείχνει τι πληρώνει η αγορά για κάθε θέση στη ναυτιλία. Είναι διαθέσιμο σε <strong>όλους τους ρόλους</strong>.</p>
        <div class="three-cards">
          <div class="role-card role-seafarer">
            <div class="role-icon">
              <svg xmlns="http://www.w3.org/2000/svg" width="28" height="28" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="1.5"><path d="M20 21v-2a4 4 0 0 0-4-4H8a4 4 0 0 0-4 4v2"/><circle cx="12" cy="7" r="4"/></svg>
            </div>
            <h4>Ναυτικός</h4>
            <p>Μάθε αν πληρώνεσαι σωστά. Σύγκρινε τον μισθό σου με την αγορά και διαπραγματεύσου καλύτερα.</p>
          </div>
          <div class="role-card role-owner">
            <div class="role-icon">
              <svg xmlns="http://www.w3.org/2000/svg" width="28" height="28" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="1.5"><line x1="12" y1="1" x2="12" y2="23"/><path d="M17 5H9.5a3.5 3.5 0 0 0 0 7h5a3.5 3.5 0 0 1 0 7H6"/></svg>
            </div>
            <h4>Πλοιοκτήτης</h4>
            <p>Δες τις τιμές αγοράς για να κάνεις competitive offers και να κρατήσεις τους καλύτερους ναυτικούς.</p>
          </div>
          <div class="role-card role-agent">
            <div class="role-icon">
              <svg xmlns="http://www.w3.org/2000/svg" width="28" height="28" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="1.5"><path d="M2 20h20"/><path d="M5 20V8l7-5 7 5v12"/><path d="M9 20v-6h6v6"/></svg>
            </div>
            <h4>Manning Agent</h4>
            <p>Ξέρε τις τιμές αγοράς για κάθε βαθμό ώστε να κάνεις σωστά offers στους ναυτικούς σου.</p>
          </div>
        </div>
      </div>
      <div class="card guide-card">
        <h3>Τι περιλαμβάνει</h3>
        <div class="feature-list">
          <div class="feat"><strong>4 Φίλτρα:</strong> Βαθμός (18 ranks), Τύπος πλοίου (7), Εθνικότητα (9), Εμπειρία (4 επίπεδα)</div>
          <div class="feat"><strong>Box Plot:</strong> Κατανομή μισθών — ελάχιστο, 25η, 50η, 75η εκατοστημόριο, μέγιστο</div>
          <div class="feat"><strong>Bar Chart:</strong> Σύγκριση median salary ανά τύπο πλοίου (LPG/LNG πληρώνει +25%)</div>
          <div class="feat"><strong>Trend Line:</strong> Εξέλιξη μισθών 2021-2026 με ετήσια αλλαγή (%)</div>
          <div class="feat"><strong>Rank Table:</strong> Πλήρης πίνακας 18 βαθμών — κλικ σε οποιονδήποτε βαθμό αλλάζει όλα τα charts</div>
          <div class="feat"><strong>Market Insights:</strong> LPG premium, nationality gap, shortage alerts, YoY growth</div>
        </div>
      </div>
      <div class="card guide-card">
        <h3>Πηγές δεδομένων</h3>
        <div class="feature-list">
          <div class="feat"><strong>HireHub Profiles:</strong> 24,847 εγγεγραμμένοι ναυτικοί με μισθολογικά στοιχεία</div>
          <div class="feat"><strong>BIMCO / ICS:</strong> Seafarer Workforce Report 2024 — παγκόσμια μισθολογική έρευνα</div>
          <div class="feat"><strong>Drewry:</strong> Manning Annual Review 2025 — ετήσια ανάλυση κόστους πληρωμάτων</div>
          <div class="feat"><strong>Market Intelligence:</strong> Δεδομένα από 180+ principals παγκοσμίως</div>
        </div>
        <p style="margin-top:var(--space-4);color:var(--color-text-tertiary);font-size:13px;">Τα δεδομένα ανανεώνονται κάθε μήνα. Τα ποσά είναι ενδεικτικά και μπορεί να διαφέρουν ανάλογα με εταιρεία, σημαία, CBA, και συνθήκες αγοράς.</p>
      </div>
    </div>

    <!-- Section 8: Γλωσσάριο -->
    <div v-if="activeSection === 8" class="guide-content">
      <div class="card guide-card highlight-card">
        <h2>Γλωσσάριο — Glossary</h2>
        <p class="lead">Ορισμοί βασικών όρων στα Ελληνικά και Αγγλικά</p>
      </div>
      <div class="card guide-card">
        <div v-for="g in glossary" :key="g.term" class="glossary-item">
          <div class="glossary-term">{{ g.term }}</div>
          <div class="glossary-desc">
            <p>{{ g.desc }}</p>
            <p class="glossary-en">{{ g.descEn }}</p>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<style scoped>
.guide-page { padding-top: var(--space-5); padding-bottom: var(--space-8); max-width: 960px; }
.guide-header { margin-bottom: var(--space-4); }
.guide-header h1 { font: var(--font-h1); display: flex; align-items: center; gap: var(--space-2); }
.guide-subtitle { font: var(--font-small); color: var(--color-text-secondary); margin-top: 4px; }

.guide-tabs { display: flex; flex-wrap: wrap; gap: 0; border-bottom: 2px solid var(--color-border); margin-bottom: var(--space-4); }
.guide-tab { padding: var(--space-2) var(--space-3); background: none; border: none; font: var(--font-small); font-weight: 500; color: var(--color-text-secondary); cursor: pointer; border-bottom: 2px solid transparent; margin-bottom: -2px; transition: all 0.15s; }
.guide-tab:hover { color: var(--color-primary); }
.guide-tab.active { color: var(--color-primary); border-bottom-color: var(--color-primary); }

.guide-card { padding: var(--space-4); margin-bottom: var(--space-3); box-shadow: 0 2px 8px rgba(0,0,0,0.06); }
.guide-card h2 { font: var(--font-h1); margin-bottom: var(--space-1); }
.guide-card h3 { font: var(--font-h2); color: var(--color-primary); margin-bottom: var(--space-2); margin-top: var(--space-3); }
.guide-card h3:first-child { margin-top: 0; }
.guide-card p { font: var(--font-body); line-height: 1.7; margin-bottom: var(--space-2); }
.lead { font: var(--font-body); color: var(--color-text-secondary); }

.highlight-card { background: var(--color-primary-light); border-left: 4px solid var(--color-primary); }
.highlight-owner { background: #FAEEDA; border-left-color: var(--color-accent); }
.highlight-agent { background: #E1F5EE; border-left-color: var(--color-agent); }
.highlight-seafarer { background: var(--color-primary-light); border-left-color: var(--color-primary); }

.three-cards { display: grid; grid-template-columns: repeat(3, 1fr); gap: var(--space-3); margin: var(--space-3) 0; }
.role-card { padding: var(--space-3); border: 1px solid var(--color-border); border-radius: var(--radius-lg); text-align: center; }
.role-card h4 { font: var(--font-h3); margin: var(--space-2) 0 var(--space-1); }
.role-card p { font: var(--font-caption); color: var(--color-text-secondary); margin: 0; }
.role-icon { display: flex; justify-content: center; }
.role-seafarer { border-color: var(--color-primary); }
.role-seafarer .role-icon { color: var(--color-primary); }
.role-owner { border-color: var(--color-accent); }
.role-owner .role-icon { color: var(--color-accent); }
.role-agent { border-color: var(--color-agent); }
.role-agent .role-icon { color: var(--color-agent); }

.info-box { background: var(--color-surface); border-left: 3px solid var(--color-primary); padding: var(--space-3); border-radius: 0 var(--radius-md) var(--radius-md) 0; margin-top: var(--space-3); font: var(--font-small); }

.steps { display: flex; flex-direction: column; gap: 0; }
.step { display: flex; gap: var(--space-3); padding: var(--space-3) 0; border-bottom: 1px solid var(--color-border); }
.step:last-child { border-bottom: none; }
.step-num { width: 36px; height: 36px; border-radius: 50%; background: var(--color-primary); color: white; display: flex; align-items: center; justify-content: center; font-size: 16px; font-weight: 600; flex-shrink: 0; }
.step-last { background: var(--color-success); }
.step-content { flex: 1; }
.step-content h4 { font: var(--font-h3); margin-bottom: 4px; }
.step-content p { font: var(--font-small); margin-bottom: 4px; }
.step-en { color: var(--color-text-tertiary); font-style: italic; }

.feature-list { display: flex; flex-direction: column; gap: var(--space-2); }
.feat { font: var(--font-small); padding: var(--space-2) 0; border-bottom: 1px solid var(--color-border); line-height: 1.6; }
.feat:last-child { border-bottom: none; }
.sub-list { margin: var(--space-2) 0 0 var(--space-4); list-style: disc; }
.sub-list li { font: var(--font-small); margin-bottom: 4px; line-height: 1.5; }

.criteria-grid { display: grid; grid-template-columns: 1fr 1fr; gap: var(--space-2); margin: var(--space-3) 0; }
.criteria-item { padding: var(--space-3); background: var(--color-surface); border-radius: var(--radius-md); }
.criteria-item strong { font: var(--font-small); display: block; margin-bottom: 4px; }
.criteria-item p { font: var(--font-caption); color: var(--color-text-secondary); margin: 0; }

.glossary-item { display: flex; gap: var(--space-3); padding: var(--space-3) 0; border-bottom: 1px solid var(--color-border); }
.glossary-item:last-child { border-bottom: none; }
.glossary-term { min-width: 140px; font: var(--font-body); font-weight: 600; color: var(--color-primary); flex-shrink: 0; }
.glossary-desc { flex: 1; }
.glossary-desc p { font: var(--font-small); margin-bottom: 4px; line-height: 1.6; }
.glossary-en { color: var(--color-text-tertiary); font-style: italic; }

@media (max-width: 768px) {
  .three-cards { grid-template-columns: 1fr; }
  .criteria-grid { grid-template-columns: 1fr; }
  .guide-tabs { overflow-x: auto; flex-wrap: nowrap; }
  .glossary-item { flex-direction: column; gap: var(--space-1); }
  .glossary-term { min-width: auto; }
}
</style>
