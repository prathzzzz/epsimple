import React from 'react';
import { motion } from 'framer-motion';
import { Logo } from '@/components/logo';

export const Navbar: React.FC = () => {
  return (
    <motion.nav
      initial={{ y: -20, opacity: 0 }}
      animate={{ y: 0, opacity: 1 }}
      className="absolute top-0 left-0 right-0 z-50 py-6 bg-white/80 backdrop-blur-md border-b border-stone-200/50"
    >
      <div className="max-w-7xl mx-auto px-6 flex items-center justify-between">
        <div className="flex items-center gap-2">
          <Logo className="h-10" variant="dark" />
        </div>

        <div className="flex items-center gap-4">
          <a href="/sign-in" className="text-sm font-medium text-stone-600 hover:text-orange-600 transition-colors">Log in</a>
        </div>
      </div>
    </motion.nav>
  );
};
