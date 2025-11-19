import React from 'react';
import { motion } from 'framer-motion';
import { GitBranch, Zap, Globe, Box } from 'lucide-react';

export const WorkflowEngine: React.FC = () => {
  return (
    <section id="workflow" className="py-32 bg-stone-50 overflow-hidden">
      <div className="max-w-7xl mx-auto px-6">
        <div className="grid grid-cols-1 lg:grid-cols-2 gap-16 items-center">
          
          {/* Text Content */}
          <div>
            <span className="text-orange-600 font-medium tracking-wider text-sm uppercase mb-4 block">The Engine</span>
            <h2 className="text-4xl md:text-5xl font-serif text-stone-900 mb-6">
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
            className="bg-white p-8 rounded-3xl border border-stone-200 shadow-xl shadow-stone-200/50 relative overflow-hidden min-h-[400px] flex flex-col justify-between"
          >
             {/* Abstract Network Visualization */}
             <div className="absolute inset-0 opacity-30">
                <svg className="w-full h-full" viewBox="0 0 400 400" xmlns="http://www.w3.org/2000/svg">
                  <pattern id="grid" width="40" height="40" patternUnits="userSpaceOnUse">
                    <path d="M 40 0 L 0 0 0 40" fill="none" stroke="#e7e5e4" strokeWidth="1"/>
                  </pattern>
                  <rect width="100%" height="100%" fill="url(#grid)" />
                </svg>
             </div>

             <div className="relative z-10 flex items-center justify-between mb-12">
                <div className="px-4 py-2 rounded-full bg-stone-100 border border-stone-200 text-xs font-medium text-stone-500 flex items-center gap-2">
                  <div className="w-2 h-2 bg-emerald-500 rounded-full animate-pulse"></div>
                  ENGINE ONLINE
                </div>
                <Globe className="text-stone-300" />
             </div>

             <div className="relative z-10 grid grid-cols-3 gap-4 items-center">
                {/* Left Node */}
                <div className="flex flex-col items-center gap-2">
                   <div className="w-12 h-12 rounded-xl bg-stone-900 text-white flex items-center justify-center shadow-lg">
                      <Box size={20} />
                   </div>
                   <span className="text-xs font-medium text-stone-500">Asset Source</span>
                </div>

                {/* Center Processing */}
                <div className="flex flex-col items-center relative">
                   <div className="absolute top-1/2 left-0 right-0 h-0.5 bg-stone-200 -z-10"></div>
                   <div className="w-16 h-16 rounded-full bg-orange-600 text-white flex items-center justify-center shadow-xl shadow-orange-500/30 relative">
                      <Zap size={24} />
                      <div className="absolute inset-0 rounded-full border-2 border-white/20 animate-ping"></div>
                   </div>
                   <span className="text-xs font-medium text-orange-600 mt-3 bg-orange-50 px-2 py-1 rounded">Processing</span>
                </div>

                {/* Right Node */}
                <div className="flex flex-col items-center gap-2">
                   <div className="w-12 h-12 rounded-xl bg-white border border-stone-200 text-stone-600 flex items-center justify-center shadow-sm">
                      <GitBranch size={20} />
                   </div>
                   <span className="text-xs font-medium text-stone-500">Distribution</span>
                </div>
             </div>

             <div className="relative z-10 mt-12 bg-stone-50 rounded-xl p-4 border border-stone-100">
                <div className="flex items-center justify-between text-xs text-stone-400 font-mono mb-2">
                   <span>TX_HASH_88219</span>
                   <span>0.024s</span>
                </div>
                <div className="h-1 w-full bg-stone-200 rounded-full overflow-hidden">
                   <div className="h-full bg-stone-800 w-2/3 animate-[width_2s_ease-in-out_infinite]"></div>
                </div>
             </div>

          </motion.div>

        </div>
      </div>
    </section>
  );
};