import React from 'react';
import { motion } from 'framer-motion';
import { GitBranch, Zap, Globe, Box } from 'lucide-react';

export const WorkflowEngine: React.FC = () => {
  return (
    <section id="workflow" className="py-20 bg-stone-50 overflow-hidden">
      <div className="max-w-7xl mx-auto px-6">
        <div className="grid grid-cols-1 lg:grid-cols-2 gap-12 items-center">
          
          {/* Text Content */}
          <div>
            <span className="text-orange-600 font-medium tracking-wider text-sm uppercase mb-4 block">The Engine</span>
            <h2 className="text-3xl md:text-4xl font-serif text-stone-900 mb-6">
              If-This-Then-That <br /> for physical assets.
            </h2>
            <p className="text-stone-600 text-lg mb-8 leading-relaxed">
              Define logic for asset movement. If an asset value exceeds $10k, require CFO approval. Streamline compliance and audit logs automatically.
            </p>
          </div>

          {/* Single Visual Block */}
          <motion.div
            initial={{ opacity: 0, scale: 0.95 }}
            whileInView={{ opacity: 1, scale: 1 }}
            transition={{ duration: 0.5 }}
            className="bg-[#0c0a09] p-8 rounded-3xl border border-stone-800 shadow-2xl shadow-black/50 relative overflow-hidden min-h-[350px] flex flex-col justify-between group"
          >
             {/* Abstract Network Visualization */}
             <div className="absolute inset-0 opacity-20">
                <svg className="w-full h-full" viewBox="0 0 400 400" xmlns="http://www.w3.org/2000/svg">
                  <pattern id="grid" width="40" height="40" patternUnits="userSpaceOnUse">
                    <path d="M 40 0 L 0 0 0 40" fill="none" stroke="#44403c" strokeWidth="1"/>
                  </pattern>
                  <rect width="100%" height="100%" fill="url(#grid)" />
                </svg>
             </div>

             <div className="relative z-10 flex items-center justify-between mb-12">
                <div className="px-4 py-2 rounded-full bg-stone-900 border border-stone-800 text-xs font-medium text-stone-400 flex items-center gap-2 shadow-inner">
                  <div className="w-2 h-2 bg-emerald-500 rounded-full animate-pulse shadow-[0_0_8px_rgba(16,185,129,0.6)]"></div>
                  ENGINE ONLINE
                </div>
                <Globe className="text-stone-600" />
             </div>

             <div className="relative z-10 grid grid-cols-3 gap-4 items-center">
                {/* Left Node */}
                <div className="flex flex-col items-center gap-2">
                   <div className="w-12 h-12 rounded-xl bg-stone-800 text-white flex items-center justify-center shadow-lg border border-stone-700 group-hover:border-orange-500/50 transition-colors duration-500">
                      <Box size={20} />
                   </div>
                   <span className="text-xs font-medium text-stone-500">Procure</span>
                </div>

                {/* Center Processing */}
                <div className="flex flex-col items-center relative">
                   {/* Connecting Line */}
                   <div className="absolute top-1/2 left-0 right-0 h-0.5 bg-stone-800 -z-10 overflow-hidden">
                      <div className="absolute top-0 left-0 h-full w-1/2 bg-gradient-to-r from-transparent via-orange-500 to-transparent animate-[shimmer_2s_infinite]"></div>
                   </div>
                   
                   <div className="w-16 h-16 rounded-full bg-orange-600 text-white flex items-center justify-center shadow-[0_0_30px_rgba(234,88,12,0.4)] relative z-10">
                      <Zap size={24} className="animate-[pulse_2s_ease-in-out_infinite]" />
                      <div className="absolute inset-0 rounded-full border-2 border-white/20 animate-ping"></div>
                      <div className="absolute inset-0 rounded-full border border-orange-400/50 animate-[spin_4s_linear_infinite]"></div>
                   </div>
                   <span className="text-xs font-medium text-orange-500 mt-3 bg-orange-950/30 px-2 py-1 rounded border border-orange-900/50">Processing</span>
                </div>

                {/* Right Node */}
                <div className="flex flex-col items-center gap-2">
                   <div className="w-12 h-12 rounded-xl bg-stone-900 border border-stone-800 text-stone-400 flex items-center justify-center shadow-sm group-hover:text-white transition-colors">
                      <GitBranch size={20} />
                   </div>
                   <span className="text-xs font-medium text-stone-500">Shift</span>
                </div>
             </div>

          </motion.div>

        </div>
      </div>
    </section>
  );
};
