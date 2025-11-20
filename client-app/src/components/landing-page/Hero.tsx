import React from 'react';
import { motion } from 'framer-motion';
import { 
  LayoutDashboard, 
  Building2, 
  Activity, 
  FolderKanban, 
  Receipt, 
  CreditCard, 
  Wallet, 
  Users, 
  UserSquare2, 
  UserCircle,
  ChevronRight,
  Search,
  Bell,
  ArrowUpRight
} from 'lucide-react';
import { Logo } from '@/components/logo';

export const Hero: React.FC = () => {
  const sidebarItems = [
    { header: "Operations", items: [
      { icon: <LayoutDashboard size={18} />, label: "Asset Management", active: true },
      { icon: <Building2 size={18} />, label: "Site Management" },
      { icon: <Activity size={18} />, label: "Activity Management" },
      { icon: <FolderKanban size={18} />, label: "Project Management" }
    ]},
    { header: "Financial", items: [
      { icon: <Receipt size={18} />, label: "Transactions" },
      { icon: <Wallet size={18} />, label: "Expenditures" },
      { icon: <CreditCard size={18} />, label: "Payment Management" }
    ]},
    { header: "People & Organizations", items: [
      { icon: <Building2 size={18} />, label: "Vendors" },
      { icon: <UserSquare2 size={18} />, label: "Landlords" },
      { icon: <Users size={18} />, label: "Persons" }
    ]}
  ];

  return (
    <section className="relative min-h-screen pt-24 pb-24 overflow-hidden bg-[#fbfaf8]">
      {/* Noise Texture Overlay */}
      <div className="absolute inset-0 opacity-[0.03] pointer-events-none" style={{ backgroundImage: `url("data:image/svg+xml,%3Csvg viewBox='0 0 200 200' xmlns='http://www.w3.org/2000/svg'%3E%3Cfilter id='noiseFilter'%3E%3CfeTurbulence type='fractalNoise' baseFrequency='0.65' numOctaves='3' stitchTiles='stitch'/%3E%3C/filter%3E%3Crect width='100%25' height='100%25' filter='url(%23noiseFilter)'/%3E%3C/svg%3E")` }}></div>
      
      {/* Warm Background Accents */}
      <div className="absolute top-0 left-1/2 -translate-x-1/2 w-full h-[800px] bg-gradient-to-b from-stone-100/80 via-white/50 to-transparent -z-10" />
      <div className="absolute top-20 right-0 w-[800px] h-[800px] bg-orange-100/40 rounded-full blur-[120px] -z-10 mix-blend-multiply opacity-60" />
      <div className="absolute bottom-0 left-0 w-[600px] h-[600px] bg-stone-200/40 rounded-full blur-[100px] -z-10 mix-blend-multiply" />

      <div className="w-full max-w-6xl mx-auto px-6 flex flex-col items-center perspective-container">
        
        <motion.div
          initial={{ opacity: 0, y: 30 }}
          animate={{ opacity: 1, y: 0 }}
          transition={{ duration: 0.8, ease: [0.16, 1, 0.3, 1] }}
          className="text-center max-w-5xl mx-auto mb-16 relative z-10"
        >
          <h1 className="text-4xl md:text-5xl lg:text-6xl font-serif font-medium text-stone-900 leading-[1.1] mb-6 tracking-tight">
            Your Complete Asset, Finance & <br />
            <span className="text-orange-600 italic">Workflow Command Center</span>
          </h1>
          
          <p className="text-lg text-stone-600 leading-relaxed max-w-3xl mx-auto">
            Monitor assets in real time, manage invoices effortlessly, and run business processes through an integrated workflow engine.
          </p>
        </motion.div>

        <div className="w-full relative" style={{ perspective: '2000px' }}>
            <motion.div
            initial={{ opacity: 0, rotateX: 20, y: 100, scale: 0.9 }}
            animate={{ opacity: 1, rotateX: 10, y: 0, scale: 1 }}
            transition={{ duration: 1.2, delay: 0.2, ease: "easeOut" }}
            className="w-full relative transform-gpu"
            style={{ transformStyle: 'preserve-3d' }}
            >
            {/* Dashboard Interface Mockup */}
            <div className="bg-[#0c0a09] rounded-2xl shadow-[0_40px_80px_-20px_rgba(0,0,0,0.4)] overflow-hidden border border-stone-800/80 w-full aspect-[16/10] min-h-[500px] flex relative z-20 ring-1 ring-white/10">
                
                {/* Sidebar */}
                <div className="w-64 bg-[#0f1115] border-r border-stone-800 flex-shrink-0 hidden lg:flex flex-col">
                <div className="p-6 flex items-center gap-3 border-b border-stone-800/50">
                    <Logo variant="light" className="h-10" />
                </div>
                
                <div className="flex-1 overflow-y-auto py-6 px-4 space-y-8 custom-scrollbar">
                    {sidebarItems.map((section, idx) => (
                    <div key={idx}>
                        <h3 className="text-[10px] font-bold text-stone-500 uppercase tracking-widest mb-3 px-3">
                        {section.header}
                        </h3>
                        <div className="space-y-1">
                        {section.items.map((item, i) => (
                            <div 
                            key={i} 
                            className={`flex items-center justify-between px-3 py-2.5 rounded-lg cursor-pointer text-sm transition-all group ${
                                item.active 
                                ? 'bg-gradient-to-r from-orange-600/10 to-transparent text-orange-500 border-l-2 border-orange-500' 
                                : 'text-stone-400 hover:text-stone-200 hover:bg-stone-800/50 border-l-2 border-transparent'
                            }`}
                            >
                            <div className="flex items-center gap-3">
                                <span className={item.active ? 'text-orange-500' : 'group-hover:text-white transition-colors'}>
                                {item.icon}
                                </span>
                                <span className="font-medium">{item.label}</span>
                            </div>
                            {item.active ? <ChevronRight size={14} /> : null}
                            </div>
                        ))}
                        </div>
                    </div>
                    ))}
                </div>
                
                <div className="p-4 border-t border-stone-800 bg-[#0a0c0f]">
                    <div className="flex items-center gap-3">
                    <div className="w-8 h-8 bg-stone-800 rounded-full flex items-center justify-center border border-stone-700">
                        <UserCircle className="text-stone-400" size={20} />
                    </div>
                    <div className="flex-1 min-w-0">
                        <div className="text-sm text-white font-medium truncate">Pratham Karia</div>
                        <div className="text-xs text-stone-500">Admin</div>
                    </div>
                    </div>
                </div>
                </div>

                {/* Main Content Area */}
                <div className="flex-1 bg-[#14161b] flex flex-col min-w-0 relative">
                {/* Glassy Top Bar */}
                <div className="h-16 border-b border-stone-800 flex items-center justify-between px-6 bg-[#14161b]/90 backdrop-blur-sm sticky top-0 z-10">
                    <div className="flex items-center gap-4">
                        <h2 className="text-white font-medium hidden md:block tracking-tight">Dashboard</h2>
                        <div className="h-5 w-px bg-stone-800 hidden md:block"></div>
                        <div className="flex items-center gap-3 text-stone-500 bg-stone-900/50 px-3 py-2 rounded-lg border border-stone-800 w-64 transition-colors focus-within:border-stone-700 focus-within:text-stone-300 focus-within:bg-stone-900">
                            <Search size={14} />
                            <input type="text" placeholder="Search assets..." className="bg-transparent border-none outline-none text-sm w-full placeholder:text-stone-600" />
                        </div>
                    </div>
                    <div className="flex items-center gap-4">
                    <div className="flex items-center gap-2 px-2.5 py-1 rounded-full bg-emerald-500/5 border border-emerald-500/20 text-emerald-500 text-xs font-medium tracking-wide">
                            <div className="w-1.5 h-1.5 rounded-full bg-emerald-500 animate-pulse" />
                            SYSTEM ONLINE
                    </div>
                    <div className="relative cursor-pointer hover:text-white text-stone-400 transition-colors">
                        <Bell size={18} />
                        <div className="absolute top-0 right-0 w-1.5 h-1.5 bg-orange-500 rounded-full border border-[#14161b]" />
                    </div>
                    </div>
                </div>

                {/* Dashboard Content */}
                <div className="p-6 lg:p-8 overflow-y-auto h-full custom-scrollbar">
                    <div className="flex items-end justify-between mb-8">
                    <div>
                        <h2 className="text-2xl font-semibold text-white mb-1 tracking-tight">Asset Overview</h2>
                        <p className="text-stone-500 text-sm">Portfolio performance summary.</p>
                    </div>
                    <div className="flex gap-3">
                        <button className="px-4 py-2 text-sm font-medium text-white bg-orange-600 rounded-lg hover:bg-orange-700 shadow-lg shadow-orange-600/20 flex items-center gap-2 transition-all hover:scale-105 active:scale-95">
                            <span>New Asset</span>
                            <ArrowUpRight size={14} />
                        </button>
                    </div>
                    </div>

                    {/* Charts Row */}
                    <div className="grid grid-cols-1 md:grid-cols-12 gap-6 mb-6">
                    {/* Card 1: Main Value */}
                    <div className="md:col-span-7 bg-[#1c1e24] p-6 rounded-xl border border-stone-800 hover:border-stone-700 transition-colors group">
                        <div className="flex justify-between items-start mb-6">
                            <div>
                                <h3 className="text-stone-400 text-xs font-bold mb-1 uppercase tracking-wider">Total Valuation</h3>
                                <div className="text-3xl font-bold text-white tracking-tight">$4,290,500.00</div>
                            </div>
                            <div className="bg-stone-800/50 p-2 rounded-lg text-stone-400 group-hover:text-white transition-colors border border-stone-800 group-hover:border-stone-700">
                                <Wallet size={18} />
                            </div>
                        </div>
                        
                        <div className="flex items-end gap-4">
                            <div className="flex-1 h-32 flex items-end gap-1.5">
                                {[35, 45, 40, 60, 55, 70, 65, 80, 75, 90, 85, 100].map((h, i) => (
                                <div key={i} className="flex-1 bg-gradient-to-t from-orange-600/10 to-orange-600 rounded-t-[2px] hover:from-orange-500/30 hover:to-orange-500 transition-all duration-300" style={{height: `${h}%`}}></div>
                                ))}
                            </div>
                        </div>
                    </div>
                    
                    {/* Card 2: Distribution */}
                    <div className="md:col-span-5 bg-[#1c1e24] p-6 rounded-xl border border-stone-800 hover:border-stone-700 transition-colors">
                        <h3 className="text-stone-400 text-xs font-bold mb-4 uppercase tracking-wider">Distribution</h3>
                        <div className="flex items-center justify-center gap-6 h-32">
                            <div className="relative w-24 h-24">
                                <svg viewBox="0 0 100 100" className="transform -rotate-90 w-full h-full">
                                    <circle cx="50" cy="50" r="40" stroke="#292524" strokeWidth="8" fill="none" />
                                    <circle cx="50" cy="50" r="40" stroke="#ea580c" strokeWidth="8" fill="none" strokeDasharray="150 251" className="animate-[dash_1.5s_ease-out_forwards]" strokeLinecap="round" />
                                    <circle cx="50" cy="50" r="40" stroke="#10b981" strokeWidth="8" fill="none" strokeDasharray="70 251" strokeDashoffset="-150" strokeLinecap="round" />
                                </svg>
                                <div className="absolute inset-0 flex items-center justify-center flex-col">
                                    <span className="text-white font-bold text-sm">342</span>
                                </div>
                            </div>
                            <div className="space-y-2">
                                <div className="flex items-center gap-2 text-xs">
                                <div className="w-2 h-2 rounded-full bg-orange-600 shadow-[0_0_8px_rgba(234,88,12,0.5)]" />
                                <span className="text-stone-300">Real Estate</span>
                                </div>
                                <div className="flex items-center gap-2 text-xs">
                                <div className="w-2 h-2 rounded-full bg-emerald-500 shadow-[0_0_8px_rgba(16,185,129,0.5)]" />
                                <span className="text-stone-300">Equities</span>
                                </div>
                                <div className="flex items-center gap-2 text-xs">
                                <div className="w-2 h-2 rounded-full bg-stone-700" />
                                <span className="text-stone-300">Other</span>
                                </div>
                            </div>
                        </div>
                    </div>
                    </div>

                    {/* Recent Activity Table */}
                    <div className="bg-[#1c1e24] rounded-xl border border-stone-800 overflow-hidden">
                    <div className="px-6 py-4 border-b border-stone-800 flex justify-between items-center">
                        <h3 className="text-white font-medium text-sm tracking-tight">Recent Transactions</h3>
                    </div>
                    <div className="p-0">
                        {[1, 2, 3].map((i) => (
                            <div key={i} className="flex items-center justify-between px-6 py-3.5 border-b border-stone-800/50 last:border-0 hover:bg-stone-800/30 transition-colors cursor-pointer group">
                            <div className="flex items-center gap-4">
                                <div className="w-8 h-8 bg-stone-800 rounded-lg flex items-center justify-center text-stone-400 group-hover:text-white group-hover:bg-stone-700 transition-colors border border-stone-700/50">
                                    {i % 2 === 0 ? <Building2 size={14} /> : <Receipt size={14} />}
                                </div>
                                <div>
                                    <div className="text-white text-sm font-medium group-hover:text-orange-500 transition-colors">
                                        {i % 2 === 0 ? 'NY Office Expansion' : 'Tech Procurement'}
                                    </div>
                                    <div className="text-[11px] text-stone-500">Today, 12:30 PM</div>
                                </div>
                            </div>
                            <div className="text-right">
                                <div className="text-white text-sm font-medium">-$12,450.00</div>
                                <div className="text-[11px] text-emerald-500">Cleared</div>
                            </div>
                            </div>
                        ))}
                    </div>
                    </div>

                </div>
                </div>
            </div>
            
            {/* Decorative glow underneath */}
            <div className="absolute -bottom-20 left-1/2 -translate-x-1/2 w-[90%] h-32 bg-orange-500/10 blur-[100px] -z-10 rounded-full pointer-events-none" />
            </motion.div>
        </div>

      </div>
    </section>
  );
};
