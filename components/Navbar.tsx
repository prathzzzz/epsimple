import React from 'react';
import { motion } from 'framer-motion';
import { APP_NAME } from '../constants';

export const Navbar: React.FC = () => {
  return (
    <motion.nav
      initial={{ y: -20, opacity: 0 }}
      animate={{ y: 0, opacity: 1 }}
      className="absolute top-0 left-0 right-0 z-50 py-6"
    >
      <div className="max-w-7xl mx-auto px-6 flex items-center justify-between">
        <div className="flex items-center gap-2">
          <div className="w-8 h-8 bg-orange-600 rounded-lg flex items-center justify-center transform rotate-3 shadow-lg shadow-orange-600/20">
             <div className="w-3 h-3 bg-white rounded-full" />
          </div>
          <span className="text-2xl font-serif font-bold tracking-tight text-stone-900">
            {APP_NAME}
          </span>
        </div>

        <div className="flex items-center gap-4">
          <a href="#" className="text-sm font-medium text-stone-900 hover:text-orange-600 transition-colors">Log in</a>
        </div>
      </div>
    </motion.nav>
  );
};