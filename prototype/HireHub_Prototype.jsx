import { useState } from "react";

const COLORS = {
  primary: "#0A66C2", primaryDark: "#004182", primaryLight: "#E6F1FB",
  accent: "#E7A33E", success: "#1B5E20", successBg: "#E8F5E9",
  warning: "#E65100", warningBg: "#FFF3E0", danger: "#B71C1C",
  agentGreen: "#1D9E75", surface: "#F5F5F5", border: "#E0E0E0",
  text: "#333", textSec: "#666", textTer: "#999", white: "#fff"
};

const Badge = ({ children, color, bg }) => (
  <span style={{ fontSize: 11, fontWeight: 600, padding: "2px 8px", borderRadius: 8, background: bg, color, whiteSpace: "nowrap" }}>{children}</span>
);

const Btn = ({ children, onClick, primary, small, style: s }) => (
  <button onClick={onClick} style={{ padding: small ? "5px 14px" : "8px 20px", borderRadius: 20, border: primary ? "none" : `1.5px solid ${COLORS.primary}`, background: primary ? COLORS.primary : "transparent", color: primary ? "#fff" : COLORS.primary, fontSize: 13, fontWeight: 500, cursor: "pointer", ...s }}>{children}</button>
);

const Card = ({ children, style: s }) => (
  <div style={{ background: COLORS.white, border: `0.5px solid ${COLORS.border}`, borderRadius: 12, overflow: "hidden", marginBottom: 12, ...s }}>{children}</div>
);

const Avatar = ({ initials, size = 40, color = COLORS.primary }) => (
  <div style={{ width: size, height: size, borderRadius: "50%", background: color, display: "flex", alignItems: "center", justifyContent: "center", color: "#fff", fontWeight: 600, fontSize: size * 0.35, flexShrink: 0 }}>{initials}</div>
);

const ScoreRing = ({ score, size = 40 }) => {
  const c = score >= 85 ? COLORS.success : score >= 70 ? COLORS.primary : COLORS.accent;
  const r = (size - 5) / 2, circ = 2 * Math.PI * r, off = circ - (score / 100) * circ;
  return (
    <svg width={size} height={size} style={{ flexShrink: 0 }}>
      <circle cx={size/2} cy={size/2} r={r} fill="none" stroke={COLORS.border} strokeWidth={2.5} />
      <circle cx={size/2} cy={size/2} r={r} fill="none" stroke={c} strokeWidth={2.5} strokeDasharray={circ} strokeDashoffset={off} strokeLinecap="round" transform={`rotate(-90 ${size/2} ${size/2})`} />
      <text x={size/2} y={size/2} textAnchor="middle" dominantBaseline="central" fontSize={size*0.32} fontWeight={600} fill={c}>{score}</text>
    </svg>
  );
};

const NAV_ITEMS = [
  { id: "feed", label: "Home" },
  { id: "search", label: "Talent Search" },
  { id: "profile", label: "Profile" },
  { id: "shortlist", label: "Shortlist" },
  { id: "agent", label: "Agent" },
  { id: "messages", label: "Messages" },
];

const CANDIDATES = [
  { id: 1, name: "Juan Dela Cruz", initials: "JD", rank: "3rd Officer", vessel: "Oil/Chemical Tanker", years: 4.5, nat: "Filipino", score: 92, verified: true, avail: "Jun 2026", certs: ["COC", "STCW", "Tanker Fam."] },
  { id: 2, name: "Andrei Popov", initials: "AP", rank: "3rd Officer", vessel: "Oil Tanker", years: 3, nat: "Ukrainian", score: 84, verified: true, avail: "Jul 2026", certs: ["COC", "STCW"] },
  { id: 3, name: "Rodel Santos", initials: "RS", rank: "3rd Officer", vessel: "Chemical Tanker", years: 5, nat: "Filipino", score: 78, verified: false, avail: "May 2026", certs: ["COC", "STCW", "GMDSS"] },
  { id: 4, name: "Dmytro Koval", initials: "DK", rank: "3rd Officer", vessel: "Bulk Carrier", years: 2, nat: "Ukrainian", score: 65, verified: true, avail: "Aug 2026", certs: ["COC"] },
  { id: 5, name: "Mark Reyes", initials: "MR", rank: "3rd Officer", vessel: "Oil/Chemical Tanker", years: 6, nat: "Filipino", score: 88, verified: true, avail: "Jun 2026", certs: ["COC", "STCW", "Tanker Fam.", "ECDIS"] },
];

const SHORTLIST_ITEMS = [
  { ...CANDIDATES[0], status: "sent_to_agent" },
  { ...CANDIDATES[4], status: "sent_to_agent" },
  { ...CANDIDATES[1], status: "under_review" },
  { ...CANDIDATES[2], status: "shortlisted" },
  { ...CANDIDATES[3], status: "shortlisted" },
];

const MESSAGES = [
  { id: 1, name: "Juan Dela Cruz", initials: "JD", preview: "Thank you for considering me for the position...", time: "2m ago", unread: true },
  { id: 2, name: "Oceanic Manning Agency", initials: "OM", preview: "We received your shortlist. Processing 2 candidates...", time: "1h ago", unread: true, color: COLORS.agentGreen },
  { id: 3, name: "Mark Reyes", initials: "MR", preview: "I have updated my availability. Available from June.", time: "3h ago", unread: false },
  { id: 4, name: "Diana Shipping HR", initials: "DS", preview: "Do you have any 2nd Officers with LPG experience?", time: "1d ago", unread: false, color: COLORS.accent },
];

const Navbar = ({ active, onNav }) => (
  <div style={{ background: COLORS.white, borderBottom: `1px solid ${COLORS.border}`, padding: "0 16px", position: "sticky", top: 0, zIndex: 10 }}>
    <div style={{ maxWidth: 900, margin: "0 auto", display: "flex", alignItems: "center", gap: 8, height: 52, overflowX: "auto" }}>
      <span style={{ fontWeight: 700, color: COLORS.primaryDark, fontSize: 18, marginRight: 16, flexShrink: 0 }}>HireHub</span>
      {NAV_ITEMS.map(n => (
        <button key={n.id} onClick={() => onNav(n.id)} style={{ padding: "6px 12px", borderRadius: 16, border: "none", background: active === n.id ? COLORS.primaryLight : "transparent", color: active === n.id ? COLORS.primary : COLORS.textSec, fontSize: 13, fontWeight: 500, cursor: "pointer", whiteSpace: "nowrap", flexShrink: 0 }}>{n.label}</button>
      ))}
    </div>
  </div>
);

// ============ SCREEN 1: HOME FEED ============
const FeedScreen = ({ onNav }) => {
  const posts = [
    { author: "Capt. Maria Santos", title: "Master Mariner", text: "Proud to announce that I have completed 25 years of sea service. From cadet to captain — never stop learning. Thank you to all my mentors and crew mates along the way.", likes: 234, comments: 45, time: "2h ago" },
    { author: "STCW Training Center Manila", title: "Maritime Training", text: "New ECDIS 2026 update training now available! All officers must complete the refresher course before December 2026. Enroll now at our Intramuros campus.", likes: 89, comments: 12, time: "5h ago" },
    { author: "Maritime Safety Weekly", title: "Industry News", text: "Red Sea routing update: Major shipping companies adjusting transit schedules. Check latest advisories from the International Maritime Organization.", likes: 156, comments: 38, time: "8h ago" },
  ];
  return (
    <div style={{ maxWidth: 900, margin: "0 auto", padding: 16, display: "grid", gridTemplateColumns: "200px 1fr 220px", gap: 16 }}>
      <div>
        <Card style={{ padding: 16, textAlign: "center" }}>
          <Avatar initials="NK" size={56} color={COLORS.accent} />
          <div style={{ fontWeight: 600, fontSize: 14, marginTop: 8 }}>Nikos K.</div>
          <div style={{ fontSize: 12, color: COLORS.textSec }}>Fleet Manager</div>
          <div style={{ fontSize: 11, color: COLORS.textTer, marginTop: 8 }}>12 vessels · 240 crew</div>
        </Card>
        <Card style={{ padding: 12 }}>
          {["Home", "Talent Search", "Shortlists", "Messages", "Settings"].map((l, i) => (
            <div key={i} onClick={() => { if (l === "Talent Search") onNav("search"); if (l === "Messages") onNav("messages"); }} style={{ padding: "8px 0", fontSize: 13, color: i === 0 ? COLORS.primary : COLORS.text, fontWeight: i === 0 ? 600 : 400, cursor: "pointer", borderLeft: i === 0 ? `3px solid ${COLORS.primary}` : "3px solid transparent", paddingLeft: 8 }}>{l}</div>
          ))}
        </Card>
      </div>
      <div>
        <Card style={{ padding: 12, display: "flex", alignItems: "center", gap: 10 }}>
          <Avatar initials="NK" size={36} color={COLORS.accent} />
          <div style={{ flex: 1, padding: "8px 12px", borderRadius: 20, background: COLORS.surface, fontSize: 13, color: COLORS.textTer }}>Start a post...</div>
        </Card>
        {posts.map((p, i) => (
          <Card key={i}>
            <div style={{ padding: 16 }}>
              <div style={{ display: "flex", gap: 10, marginBottom: 10 }}>
                <Avatar initials={p.author.split(" ").map(w=>w[0]).join("")} size={40} color={i === 1 ? COLORS.agentGreen : COLORS.primary} />
                <div><div style={{ fontWeight: 600, fontSize: 14 }}>{p.author}</div><div style={{ fontSize: 12, color: COLORS.textSec }}>{p.title} · {p.time}</div></div>
              </div>
              <div style={{ fontSize: 14, lineHeight: 1.5, color: COLORS.text }}>{p.text}</div>
              <div style={{ marginTop: 12, paddingTop: 10, borderTop: `1px solid ${COLORS.border}`, display: "flex", gap: 20, fontSize: 12, color: COLORS.textSec }}>
                <span>{p.likes} likes</span><span>{p.comments} comments</span><span>Share</span>
              </div>
            </div>
          </Card>
        ))}
      </div>
      <div>
        <Card style={{ padding: 12 }}>
          <div style={{ fontWeight: 600, fontSize: 13, marginBottom: 8 }}>Trending</div>
          {["ECDIS 2026 updates", "Red Sea routing", "Decarbonization IMO", "Seafarer mental health"].map((t, i) => (
            <div key={i} style={{ padding: "6px 0", fontSize: 12, color: COLORS.text, borderBottom: i < 3 ? `1px solid ${COLORS.surface}` : "none" }}>{t}</div>
          ))}
        </Card>
        <Card style={{ padding: 12 }}>
          <div style={{ fontWeight: 600, fontSize: 13, marginBottom: 8 }}>Latest openings</div>
          {[{t:"2nd Officer — LPG", c:"Dorian LPG"},{t:"Chief Engineer — Bulk", c:"Diana Shipping"},{t:"Master — Container", c:"Costamare"}].map((j, i) => (
            <div key={i} style={{ padding: "6px 0", fontSize: 12, borderBottom: i < 2 ? `1px solid ${COLORS.surface}` : "none" }}>
              <div style={{ fontWeight: 500, color: COLORS.primary }}>{j.t}</div>
              <div style={{ color: COLORS.textSec, fontSize: 11 }}>{j.c}</div>
            </div>
          ))}
        </Card>
      </div>
    </div>
  );
};

// ============ SCREEN 2: TALENT SEARCH ============
const SearchScreen = ({ onNav }) => (
  <div style={{ maxWidth: 900, margin: "0 auto", padding: 16, display: "grid", gridTemplateColumns: "220px 1fr", gap: 16 }}>
    <div>
      <Card style={{ padding: 16 }}>
        <div style={{ fontWeight: 600, fontSize: 14, marginBottom: 12, color: COLORS.primaryDark }}>Filters</div>
        {[{l:"Rank",v:"3rd Officer"},{l:"Vessel type",v:"Oil/Chemical Tanker"},{l:"Min. sea service",v:"2+ years"},{l:"Nationality",v:"Any"},{l:"Availability",v:"Within 60 days"}].map((f, i) => (
          <div key={i} style={{ marginBottom: 12 }}>
            <div style={{ fontSize: 11, color: COLORS.textSec, marginBottom: 4 }}>{f.l}</div>
            <div style={{ padding: "7px 10px", borderRadius: 6, border: `1px solid ${COLORS.border}`, fontSize: 13, background: COLORS.white }}>{f.v}</div>
          </div>
        ))}
        <label style={{ display: "flex", alignItems: "center", gap: 6, fontSize: 12, marginBottom: 6, cursor: "pointer" }}>
          <input type="checkbox" defaultChecked style={{ accentColor: COLORS.primary }} /> Verified only
        </label>
        <label style={{ display: "flex", alignItems: "center", gap: 6, fontSize: 12, marginBottom: 12, cursor: "pointer" }}>
          <input type="checkbox" style={{ accentColor: COLORS.primary }} /> With flag endorsement
        </label>
        <Btn primary style={{ width: "100%" }}>Search</Btn>
        <div style={{ textAlign: "center", marginTop: 8 }}>
          <span style={{ fontSize: 12, color: COLORS.textSec, cursor: "pointer" }}>Reset filters</span>
        </div>
      </Card>
    </div>
    <div>
      <Card style={{ padding: "10px 16px", background: COLORS.primaryLight, border: `1px solid ${COLORS.primary}22` }}>
        <div style={{ fontSize: 13, color: COLORS.primaryDark }}>AI matching is active. Candidates ranked by compatibility with your fleet requirements.</div>
      </Card>
      <div style={{ display: "flex", justifyContent: "space-between", alignItems: "center", margin: "12px 0 8px" }}>
        <span style={{ fontSize: 14, fontWeight: 600 }}>143 candidates found</span>
        <span style={{ fontSize: 12, color: COLORS.textSec }}>Sort: AI match score</span>
      </div>
      {CANDIDATES.map(c => (
        <Card key={c.id} style={{ cursor: "pointer" }} onClick={() => onNav("profile")}>
          <div style={{ padding: 14, display: "flex", alignItems: "center", gap: 12 }}>
            <Avatar initials={c.initials} size={44} />
            <div style={{ flex: 1, minWidth: 0 }}>
              <div style={{ display: "flex", alignItems: "center", gap: 6 }}>
                <span style={{ fontWeight: 600, fontSize: 14 }}>{c.name}</span>
                {c.verified && <Badge color={COLORS.success} bg={COLORS.successBg}>Verified</Badge>}
              </div>
              <div style={{ fontSize: 12, color: COLORS.textSec }}>{c.rank} · {c.vessel} · {c.years} yrs</div>
              <div style={{ display: "flex", gap: 4, marginTop: 4, flexWrap: "wrap" }}>
                <Badge color={COLORS.textSec} bg={COLORS.surface}>{c.nat}</Badge>
                {c.certs.slice(0,2).map((cert, i) => <Badge key={i} color={COLORS.textSec} bg={COLORS.surface}>{cert}</Badge>)}
                <Badge color={COLORS.textSec} bg={COLORS.surface}>{c.avail}</Badge>
              </div>
            </div>
            <ScoreRing score={c.score} />
            <Btn small primary onClick={e => { e.stopPropagation(); onNav("shortlist"); }}>+ Shortlist</Btn>
          </div>
        </Card>
      ))}
    </div>
  </div>
);

// ============ SCREEN 3: SEAFARER PROFILE ============
const ProfileScreen = ({ onNav }) => {
  const p = CANDIDATES[0];
  return (
    <div style={{ maxWidth: 900, margin: "0 auto", padding: 16, display: "grid", gridTemplateColumns: "1fr 260px", gap: 16 }}>
      <div>
        <Card>
          <div style={{ height: 80, background: COLORS.primaryDark }} />
          <div style={{ padding: "0 20px 20px", marginTop: -30 }}>
            <Avatar initials={p.initials} size={72} />
            <div style={{ display: "flex", alignItems: "center", gap: 8, marginTop: 8 }}>
              <span style={{ fontWeight: 700, fontSize: 20 }}>{p.name}</span>
              <Badge color={COLORS.success} bg={COLORS.successBg}>Verified</Badge>
            </div>
            <div style={{ fontSize: 14, color: COLORS.textSec }}>{p.rank} · {p.vessel} · {p.years} years of sea service</div>
            <div style={{ fontSize: 13, color: COLORS.textTer }}>Manila, Philippines</div>
            <div style={{ display: "flex", gap: 12, margin: "10px 0", fontSize: 12, color: COLORS.textSec }}>
              <span>347 profile views</span><span>12 shortlists</span><span>3 inquiries</span>
            </div>
            <div style={{ display: "flex", gap: 8 }}>
              <Btn primary onClick={() => onNav("shortlist")}>Add to Shortlist</Btn>
              <Btn onClick={() => onNav("messages")}>Message</Btn>
              <Btn>Download CV</Btn>
            </div>
          </div>
        </Card>
        <Card style={{ padding: 20 }}>
          <div style={{ fontWeight: 600, fontSize: 16, marginBottom: 14 }}>Sea service record</div>
          {[
            { rank: "3rd Officer", vessel: "MT Pacific Voyager", type: "Oil/Chemical Tanker · 115,000 DWT · Marshall Islands", dates: "Mar 2024 – Present", company: "Tsakos Energy Navigation", current: true },
            { rank: "3rd Officer", vessel: "MT Aegean Star", type: "Oil Tanker · 95,000 DWT · Liberia", dates: "Jan 2023 – Feb 2024", company: "Diana Shipping" },
            { rank: "Deck Cadet", vessel: "MV Global Carrier", type: "Bulk Carrier · 82,000 DWT · Panama", dates: "Jun 2021 – Dec 2022", company: "Capital Ship Mgmt", cadet: true },
          ].map((s, i) => (
            <div key={i} style={{ display: "flex", gap: 12, marginBottom: 16 }}>
              <div style={{ display: "flex", flexDirection: "column", alignItems: "center" }}>
                <div style={{ width: 10, height: 10, borderRadius: "50%", background: s.cadet ? COLORS.accent : COLORS.primary, marginTop: 4 }} />
                {i < 2 && <div style={{ width: 1.5, flex: 1, background: COLORS.border, marginTop: 4 }} />}
              </div>
              <div>
                <div style={{ fontWeight: 600, fontSize: 14 }}>{s.rank} — {s.vessel} {s.current && <Badge color={COLORS.success} bg={COLORS.successBg}>Current</Badge>}</div>
                <div style={{ fontSize: 12, color: COLORS.textSec }}>{s.type}</div>
                <div style={{ fontSize: 12, color: COLORS.textTer }}>{s.dates} · {s.company}</div>
              </div>
            </div>
          ))}
        </Card>
        <Card style={{ padding: 20 }}>
          <div style={{ fontWeight: 600, fontSize: 16, marginBottom: 14 }}>Certificates & licenses</div>
          {[
            { name: "Certificate of Competency (COC)", issuer: "MARINA", status: "valid", exp: "Dec 2027", verified: true },
            { name: "STCW Basic Safety Training", issuer: "MARINA", status: "valid", exp: "Mar 2027", verified: true },
            { name: "Tanker Familiarization (STCW)", issuer: "STCW Training Center", status: "valid", exp: "Sep 2026", verified: true },
            { name: "Flag State Endorsement — Marshall Islands", issuer: "Marshall Islands Maritime", status: "expiring", exp: "Jul 2026", verified: false },
          ].map((c, i) => (
            <div key={i} style={{ display: "flex", justifyContent: "space-between", alignItems: "center", padding: "8px 0", borderBottom: i < 3 ? `1px solid ${COLORS.surface}` : "none" }}>
              <div>
                <div style={{ fontSize: 13, fontWeight: 500 }}>{c.name} {c.verified && <Badge color={COLORS.success} bg={COLORS.successBg}>AI Verified</Badge>}</div>
                <div style={{ fontSize: 11, color: COLORS.textSec }}>{c.issuer} · Expires {c.exp}</div>
              </div>
              <Badge color={c.status === "valid" ? COLORS.success : COLORS.warning} bg={c.status === "valid" ? COLORS.successBg : COLORS.warningBg}>{c.status === "valid" ? "Valid" : "Expiring"}</Badge>
            </div>
          ))}
        </Card>
        <Card style={{ padding: 20 }}>
          <div style={{ fontWeight: 600, fontSize: 16, marginBottom: 10 }}>Skills & endorsements</div>
          <div style={{ display: "flex", gap: 6, flexWrap: "wrap" }}>
            {["ECDIS", "GMDSS", "Cargo operations", "Bridge watch", "ISM/ISPS", "Tanker safety", "MARPOL", "English"].map(s => (
              <Badge key={s} color={COLORS.primaryDark} bg={COLORS.primaryLight}>{s}</Badge>
            ))}
          </div>
        </Card>
      </div>
      <div>
        <Card style={{ padding: 16, textAlign: "center" }}>
          <div style={{ fontSize: 12, color: COLORS.textSec, marginBottom: 6 }}>AI compatibility score</div>
          <ScoreRing score={92} size={80} />
          <div style={{ fontSize: 11, color: COLORS.textTer, marginTop: 4 }}>Based on your search criteria</div>
        </Card>
        <Card style={{ padding: 16 }}>
          <div style={{ fontWeight: 600, fontSize: 13, marginBottom: 10 }}>Availability</div>
          {[{l:"Available from",v:"June 2026"},{l:"Contract pref.",v:"Contract"},{l:"Desired rank",v:"2nd Officer"},{l:"Vessel types",v:"Oil/Chem, LPG"}].map((r, i) => (
            <div key={i} style={{ display: "flex", justifyContent: "space-between", padding: "5px 0", fontSize: 12, borderBottom: i < 3 ? `1px solid ${COLORS.surface}` : "none" }}>
              <span style={{ color: COLORS.textSec }}>{r.l}</span><span style={{ fontWeight: 500 }}>{r.v}</span>
            </div>
          ))}
        </Card>
        <Card style={{ padding: 16 }}>
          <div style={{ fontWeight: 600, fontSize: 13, marginBottom: 8 }}>Education</div>
          <div style={{ fontSize: 13 }}>Philippine Merchant Marine Academy</div>
          <div style={{ fontSize: 12, color: COLORS.textSec }}>BS Marine Transportation, 2017–2021</div>
        </Card>
        <Card style={{ padding: 16 }}>
          <div style={{ fontWeight: 600, fontSize: 13, marginBottom: 8 }}>Profile completeness</div>
          <div style={{ height: 6, borderRadius: 3, background: COLORS.surface, overflow: "hidden" }}>
            <div style={{ width: "92%", height: "100%", borderRadius: 3, background: COLORS.primary }} />
          </div>
          <div style={{ fontSize: 11, color: COLORS.textSec, marginTop: 4 }}>92% complete</div>
        </Card>
      </div>
    </div>
  );
};

// ============ SCREEN 4: SHORTLIST & PIPELINE ============
const ShortlistScreen = ({ onNav }) => {
  const [tab, setTab] = useState("all");
  const statusMap = { shortlisted: { label: "Shortlisted", color: COLORS.primary, bg: COLORS.primaryLight }, under_review: { label: "Under review", color: COLORS.warning, bg: COLORS.warningBg }, sent_to_agent: { label: "Sent to agent", color: COLORS.success, bg: COLORS.successBg } };
  const tabs = [{ id: "all", label: "All", count: 5 }, { id: "shortlisted", label: "Shortlisted", count: 2 }, { id: "under_review", label: "Under review", count: 1 }, { id: "sent_to_agent", label: "Sent to agent", count: 2 }];
  const filtered = tab === "all" ? SHORTLIST_ITEMS : SHORTLIST_ITEMS.filter(c => c.status === tab);
  return (
    <div style={{ maxWidth: 900, margin: "0 auto", padding: 16 }}>
      <Card style={{ padding: 16 }}>
        <div style={{ display: "flex", justifyContent: "space-between", alignItems: "center", marginBottom: 14 }}>
          <div>
            <div style={{ fontWeight: 700, fontSize: 18 }}>3rd Officer — Oil/Chemical Tanker</div>
            <div style={{ fontSize: 13, color: COLORS.textSec }}>MT Pacific Voyager replacement · Created Apr 2, 2026</div>
          </div>
          <div style={{ display: "flex", gap: 8 }}>
            <Btn>Compare</Btn>
            <Btn primary onClick={() => onNav("agent")}>Forward to Agent</Btn>
          </div>
        </div>
        <div style={{ display: "flex", gap: 4, marginBottom: 16 }}>
          {tabs.map(t => (
            <button key={t.id} onClick={() => setTab(t.id)} style={{ padding: "6px 14px", borderRadius: 16, border: "none", background: tab === t.id ? COLORS.primaryLight : COLORS.surface, color: tab === t.id ? COLORS.primary : COLORS.textSec, fontSize: 13, fontWeight: 500, cursor: "pointer" }}>{t.label} ({t.count})</button>
          ))}
        </div>
        {filtered.map(c => {
          const st = statusMap[c.status];
          return (
            <div key={c.id} style={{ display: "flex", alignItems: "center", gap: 12, padding: "10px 0", borderBottom: `1px solid ${COLORS.surface}`, cursor: "pointer" }} onClick={() => onNav("profile")}>
              <input type="checkbox" style={{ accentColor: COLORS.primary }} />
              <Avatar initials={c.initials} size={36} />
              <div style={{ flex: 1 }}>
                <div style={{ fontWeight: 500, fontSize: 14 }}>{c.name}</div>
                <div style={{ fontSize: 12, color: COLORS.textSec }}>{c.rank} · {c.years} yrs · {c.nat} · {c.avail}</div>
              </div>
              <ScoreRing score={c.score} size={36} />
              <Badge color={st.color} bg={st.bg}>{st.label}</Badge>
            </div>
          );
        })}
      </Card>
      <Card style={{ padding: 16 }}>
        <div style={{ fontWeight: 600, fontSize: 14, marginBottom: 10 }}>Manning agent</div>
        <div style={{ display: "flex", alignItems: "center", gap: 10, marginBottom: 10 }}>
          <Avatar initials="OM" size={36} color={COLORS.agentGreen} />
          <div>
            <div style={{ fontWeight: 500, fontSize: 14 }}>Oceanic Manning Agency</div>
            <div style={{ fontSize: 12, color: COLORS.textSec }}>DMW License: 082-LB-061524 · Manila</div>
          </div>
          <Btn small style={{ marginLeft: "auto" }}>Change agent</Btn>
        </div>
        <div style={{ display: "grid", gridTemplateColumns: "repeat(4, 1fr)", gap: 10 }}>
          {[{l:"Total candidates",v:"5"},{l:"Sent to agent",v:"2"},{l:"Under review",v:"1"},{l:"Avg AI score",v:"81.4"}].map((m, i) => (
            <div key={i} style={{ textAlign: "center", padding: 10, background: COLORS.surface, borderRadius: 8 }}>
              <div style={{ fontSize: 18, fontWeight: 600, color: COLORS.primaryDark }}>{m.v}</div>
              <div style={{ fontSize: 11, color: COLORS.textSec }}>{m.l}</div>
            </div>
          ))}
        </div>
      </Card>
    </div>
  );
};

// ============ SCREEN 5: AGENT DASHBOARD ============
const AgentScreen = () => {
  const requests = [
    { owner: "Tsakos Energy Navigation", initials: "TE", rank: "3rd Officer", vessel: "MT Pacific Voyager", type: "Oil/Chemical Tanker · 115k DWT", date: "Jun 15, 2026", candidates: 2, urgency: "Urgent", color: COLORS.accent },
    { owner: "Diana Shipping", initials: "DS", rank: "Chief Officer", vessel: "MV Diana Explorer", type: "Bulk Carrier · 82k DWT", date: "Jul 1, 2026", candidates: 3, urgency: "Standard", color: COLORS.accent },
    { owner: "Capital Ship Management", initials: "CS", rank: "2nd Engineer", vessel: "MT Capital Star", type: "LPG · 45k DWT", date: "Jun 20, 2026", candidates: 1, urgency: "Critical", color: COLORS.accent },
  ];
  return (
    <div style={{ maxWidth: 900, margin: "0 auto", padding: 16 }}>
      <Card style={{ padding: 16 }}>
        <div style={{ display: "flex", alignItems: "center", gap: 10, marginBottom: 16 }}>
          <Avatar initials="OM" size={48} color={COLORS.agentGreen} />
          <div>
            <div style={{ fontWeight: 700, fontSize: 18 }}>Oceanic Manning Agency</div>
            <div style={{ fontSize: 13, color: COLORS.textSec }}>DMW License: 082-LB-061524 · 8 principals · 487 deployments YTD</div>
          </div>
        </div>
        <div style={{ display: "grid", gridTemplateColumns: "repeat(4, 1fr)", gap: 10, marginBottom: 16 }}>
          {[{l:"New requests",v:"6",c:COLORS.danger},{l:"Active pipelines",v:"12",c:COLORS.primary},{l:"In review",v:"23",c:COLORS.accent},{l:"Placement rate",v:"94%",c:COLORS.success}].map((m, i) => (
            <div key={i} style={{ textAlign: "center", padding: 14, background: COLORS.surface, borderRadius: 8 }}>
              <div style={{ fontSize: 22, fontWeight: 700, color: m.c }}>{m.v}</div>
              <div style={{ fontSize: 11, color: COLORS.textSec }}>{m.l}</div>
            </div>
          ))}
        </div>
      </Card>
      <div style={{ fontWeight: 600, fontSize: 16, margin: "16px 0 8px" }}>Incoming shortlists</div>
      {requests.map((r, i) => (
        <Card key={i} style={{ padding: 16 }}>
          <div style={{ display: "flex", alignItems: "center", gap: 12 }}>
            <Avatar initials={r.initials} size={40} color={r.color} />
            <div style={{ flex: 1 }}>
              <div style={{ fontWeight: 600, fontSize: 14 }}>{r.owner}</div>
              <div style={{ fontSize: 13, color: COLORS.text }}>{r.rank} — {r.vessel}</div>
              <div style={{ fontSize: 12, color: COLORS.textSec }}>{r.type} · Crew change: {r.date}</div>
              <div style={{ display: "flex", gap: 6, marginTop: 6 }}>
                <Badge color={COLORS.primaryDark} bg={COLORS.primaryLight}>{r.candidates} candidates</Badge>
                <Badge color={r.urgency === "Critical" ? COLORS.danger : r.urgency === "Urgent" ? COLORS.warning : COLORS.textSec} bg={r.urgency === "Critical" ? "#FFEBEE" : r.urgency === "Urgent" ? COLORS.warningBg : COLORS.surface}>{r.urgency}</Badge>
              </div>
            </div>
            <div style={{ display: "flex", gap: 6 }}>
              <Btn primary small>Review</Btn>
              <Btn small>Message</Btn>
            </div>
          </div>
        </Card>
      ))}
      <div style={{ fontWeight: 600, fontSize: 16, margin: "16px 0 8px" }}>Deployment pipeline</div>
      <div style={{ display: "grid", gridTemplateColumns: "repeat(4, 1fr)", gap: 10 }}>
        {[{l:"Received",v:3,c:COLORS.primaryLight},{l:"Vetting",v:5,c:COLORS.primaryLight},{l:"Contract",v:2,c:COLORS.successBg},{l:"Deployment",v:2,c:COLORS.successBg}].map((s, i) => (
          <Card key={i} style={{ padding: 14, textAlign: "center", background: s.c }}>
            <div style={{ fontSize: 22, fontWeight: 700, color: COLORS.primaryDark }}>{s.v}</div>
            <div style={{ fontSize: 12, color: COLORS.textSec }}>{s.l}</div>
          </Card>
        ))}
      </div>
    </div>
  );
};

// ============ SCREEN 6: MESSAGING ============
const MessagesScreen = () => {
  const [active, setActive] = useState(0);
  const chat = [
    { from: "me", text: "Hello Juan, we reviewed your profile and are impressed with your tanker experience. We have an opening for 3rd Officer on MT Pacific Voyager.", time: "10:30 AM" },
    { from: "them", text: "Thank you for considering me for the position. I am very interested and available from June 2026. I have all required certificates up to date.", time: "10:45 AM" },
    { from: "me", text: "Excellent. I will forward your details to our manning agent, Oceanic Manning Agency, for contract processing. They will contact you shortly.", time: "11:02 AM" },
  ];
  return (
    <div style={{ maxWidth: 900, margin: "0 auto", padding: 16, display: "grid", gridTemplateColumns: "280px 1fr", gap: 0, height: "calc(100vh - 84px)" }}>
      <div style={{ background: COLORS.white, borderRight: `1px solid ${COLORS.border}`, borderRadius: "12px 0 0 12px", overflow: "auto" }}>
        <div style={{ padding: 12 }}>
          <div style={{ padding: "8px 12px", borderRadius: 8, background: COLORS.surface, fontSize: 13, color: COLORS.textTer, marginBottom: 8 }}>Search messages...</div>
        </div>
        {MESSAGES.map((m, i) => (
          <div key={m.id} onClick={() => setActive(i)} style={{ display: "flex", gap: 10, padding: "10px 12px", cursor: "pointer", background: active === i ? COLORS.primaryLight : "transparent", borderLeft: active === i ? `3px solid ${COLORS.primary}` : "3px solid transparent" }}>
            <Avatar initials={m.initials} size={36} color={m.color || COLORS.primary} />
            <div style={{ flex: 1, minWidth: 0 }}>
              <div style={{ display: "flex", justifyContent: "space-between" }}>
                <span style={{ fontWeight: m.unread ? 700 : 400, fontSize: 13 }}>{m.name}</span>
                <span style={{ fontSize: 11, color: COLORS.textTer }}>{m.time}</span>
              </div>
              <div style={{ fontSize: 12, color: COLORS.textSec, overflow: "hidden", textOverflow: "ellipsis", whiteSpace: "nowrap" }}>{m.preview}</div>
            </div>
            {m.unread && <div style={{ width: 8, height: 8, borderRadius: "50%", background: COLORS.primary, flexShrink: 0, marginTop: 4 }} />}
          </div>
        ))}
      </div>
      <div style={{ background: COLORS.white, borderRadius: "0 12px 12px 0", display: "flex", flexDirection: "column" }}>
        <div style={{ padding: "12px 16px", borderBottom: `1px solid ${COLORS.border}`, display: "flex", alignItems: "center", gap: 10 }}>
          <Avatar initials="JD" size={36} />
          <div><div style={{ fontWeight: 600, fontSize: 14 }}>Juan Dela Cruz</div><div style={{ fontSize: 12, color: COLORS.success }}>Online</div></div>
        </div>
        <div style={{ padding: "8px 16px", background: COLORS.primaryLight, fontSize: 12, color: COLORS.primaryDark }}>
          Juan is on your shortlist for MT Pacific Voyager (3rd Officer)
        </div>
        <div style={{ flex: 1, padding: 16, overflowY: "auto" }}>
          <div style={{ textAlign: "center", fontSize: 11, color: COLORS.textTer, marginBottom: 16 }}>Today</div>
          {chat.map((msg, i) => (
            <div key={i} style={{ display: "flex", justifyContent: msg.from === "me" ? "flex-end" : "flex-start", marginBottom: 12 }}>
              <div style={{ maxWidth: "70%", padding: "10px 14px", borderRadius: 16, background: msg.from === "me" ? COLORS.primary : COLORS.surface, color: msg.from === "me" ? "#fff" : COLORS.text, fontSize: 13, lineHeight: 1.5 }}>
                {msg.text}
                <div style={{ fontSize: 10, marginTop: 4, opacity: 0.7, textAlign: "right" }}>{msg.time}</div>
              </div>
            </div>
          ))}
        </div>
        <div style={{ padding: 12, borderTop: `1px solid ${COLORS.border}`, display: "flex", gap: 8 }}>
          <div style={{ flex: 1, padding: "10px 16px", borderRadius: 24, background: COLORS.surface, fontSize: 13, color: COLORS.textTer }}>Type a message...</div>
          <div style={{ width: 36, height: 36, borderRadius: "50%", background: COLORS.primary, display: "flex", alignItems: "center", justifyContent: "center", cursor: "pointer" }}>
            <svg width="16" height="16" viewBox="0 0 24 24" fill="none" stroke="#fff" strokeWidth="2"><path d="M22 2L11 13M22 2l-7 20-4-9-9-4 20-7z"/></svg>
          </div>
        </div>
      </div>
    </div>
  );
};

// ============ MAIN APP ============
export default function App() {
  const [page, setPage] = useState("feed");
  return (
    <div style={{ background: COLORS.surface, minHeight: "100vh", fontFamily: "-apple-system, BlinkMacSystemFont, 'Segoe UI', sans-serif" }}>
      <Navbar active={page} onNav={setPage} />
      {page === "feed" && <FeedScreen onNav={setPage} />}
      {page === "search" && <SearchScreen onNav={setPage} />}
      {page === "profile" && <ProfileScreen onNav={setPage} />}
      {page === "shortlist" && <ShortlistScreen onNav={setPage} />}
      {page === "agent" && <AgentScreen />}
      {page === "messages" && <MessagesScreen />}
    </div>
  );
}
